package org.nmng.library.manager.service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.nmng.library.manager.dao.RequestDetailRepository;
import org.nmng.library.manager.dao.RequestRepository;
import org.nmng.library.manager.dao.RequestStatusRepository;
import org.nmng.library.manager.dto.request.CreateRequestDto;
import org.nmng.library.manager.dto.response.RequestDto;
import org.nmng.library.manager.entity.*;
import org.nmng.library.manager.model.Policy;
import org.nmng.library.manager.service.BookService;
import org.nmng.library.manager.service.RequestService;
import org.nmng.library.manager.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class RequestServiceImpl implements RequestService {
    private final UserService userService;
    private final BookService bookService;
    private final RequestRepository requestRepository;
    private final RequestStatusRepository statusRepository;
    private final RequestDetailRepository requestDetailRepository;
    private final RequestStatus BORROWING_STATUS;
    private final RequestStatus RETURNED_STATUS;
    private final RequestStatus EXPIRED_STATUS;
    private final RequestStatus CANCELLED_STATUS;

    public RequestServiceImpl(UserService userService, BookService bookService, RequestRepository requestRepository,
                              RequestStatusRepository statusRepository, RequestDetailRepository requestDetailRepository) {
        this.userService = userService;
        this.bookService = bookService;
        this.requestRepository = requestRepository;
        this.statusRepository = statusRepository;
        this.requestDetailRepository = requestDetailRepository;

        this.BORROWING_STATUS = this.statusRepository.findByNameIgnoreCase(RequestStatus.BORROWING);
        this.RETURNED_STATUS = this.statusRepository.findByNameIgnoreCase(RequestStatus.RETURNED);
        this.EXPIRED_STATUS = this.statusRepository.findByNameIgnoreCase(RequestStatus.EXPIRED);
        this.CANCELLED_STATUS = this.statusRepository.findByNameIgnoreCase(RequestStatus.CANCELLED);
    }

    @Override
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(this.requestRepository.findAll().stream().map(RequestDto::new).toList());
    }

    @Override
    public ResponseEntity<?> getSpecifiedOne(String identifiable) {
        Request request = this.find(identifiable);

        return request == null ?
                ResponseEntity.notFound().build()
                :
                ResponseEntity.ok(new RequestDto(request));
    }

    @Override
    public ResponseEntity<?> getByPatronAndStatus(String patronIdentifiable, String statusName) {
        RequestStatus status = null;

        if (statusName != null) {
            status = this.statusRepository.findByNameIgnoreCase(statusName);

            if (status == null) return ResponseEntity.status(404).body("status not found");
        }

        if (patronIdentifiable == null) { // statusName must be not null
            List<Request> requests = this.requestRepository.findByStatus(status);
            return ResponseEntity.ok(requests.stream().map(RequestDto::new).toList());
        }

        User patron = this.userService.findUser(patronIdentifiable);

        if (patron == null) return ResponseEntity.status(404).body("patron not found");

        List<Request> requests = status == null ?
                this.requestRepository.findByPatron(patron)
                :
                this.requestRepository.findByPatronAndStatus(patron, status);

        return ResponseEntity.ok(requests.stream().map(RequestDto::new).toList());
    }

    @Override
    public ResponseEntity<?> create(CreateRequestDto dto) {
        Request request = dto.toBaseRequest();
        User patron = this.userService.findUser(dto.getUser());

        if (patron == null) { // throw here
            return ResponseEntity.status(404).body("patron not found");
        }

        // validate the provided books and return a transient list (the request field hasn't been set yet)
        List<RequestDetail> transientDetails = dto.getBooks().stream().map(inputBook -> {
            Book book = this.bookService.findBook(inputBook.getId());

            if (book == null) throw new RuntimeException("book \"%s\" is not found".formatted(inputBook.getId()));

            if (inputBook.getQuantity() > book.getQuantity()) {
                throw new ResponseStatusException(400, "%s exceeded the available number".formatted(inputBook.getQuantity()), null);
            }

            return new RequestDetail(null, book, inputBook.getQuantity());
        }).toList();

        request.setPatron(patron);
        request.setStatus(this.BORROWING_STATUS);
//        System.out.println(request); // infinitive call toUser and UserRole ???
        // First: save request
        this.requestRepository.save(request);

        // Second: save request details then return a list of the saved instances
        List<RequestDetail> details = transientDetails.stream().map(detail -> {
            detail.getBook().subtractQuantity(detail.getQuantity()); // check this

            detail.setRequest(request);
            detail = this.requestDetailRepository.save(detail);

            return detail;
        }).toList();

        // Third: set the details again to the request to response to client
        request.setDetails(details); // save the list of details back to produce output

        return ResponseEntity.status(201).body(new RequestDto(request));
    }

    @Override
    public ResponseEntity<?> completeRequest(String requestIdentifiable) {
        Request request = this.find(requestIdentifiable);

        if (request == null) return ResponseEntity.notFound().build();
        if (!(request.isBorrowing() || request.hasExpired())) {
            return ResponseEntity.badRequest().body("request is not in borrowing state");
        }

        List<RequestDetail> details = this.requestDetailRepository.findByRequest(request);

        details.forEach(detail -> {
            Book book = this.bookService.findBook(String.valueOf(detail.getBook().getId()));
            book.addQuantity(detail.getQuantity());
        });

        request.setStatus(this.RETURNED_STATUS);


        return ResponseEntity.ok(new RequestDto(request));
    }

    @Override
    public ResponseEntity<?> cancelRequest(String requestIdentifiable) {
        Request request = this.find(requestIdentifiable);

        if (request == null) return ResponseEntity.notFound().build();
        if (!request.isBorrowing()) return ResponseEntity.badRequest().body("request is not in borrowing state");

        List<RequestDetail> details = this.requestDetailRepository.findByRequest(request);
//        List<Book> books = new ArrayList<>();
        details.forEach(detail -> {
            Book book = this.bookService.findBook(String.valueOf(detail.getBook().getId()));
            book.addQuantity(detail.getQuantity());
        });

        request.setStatus(this.CANCELLED_STATUS);

        return ResponseEntity.ok(new RequestDto(request));
    }

//    @Scheduled(fixedRate = 86400)
//    @Override
    public void updateExpiredRequest() {
        List<Request> expiredRequests = this.requestRepository.findByStatus(this.EXPIRED_STATUS);
        List<Request> borrowingRequests = this.requestRepository.findByStatus(this.BORROWING_STATUS);

        expiredRequests.forEach(request -> {
            if (request.getFine() == 0) { // rare case; mainly for testing against some initial invalid data
                long duration = Duration.between(request.getDueDate(), request.getUpdateTime()).getSeconds();

                long numberOfIntervals = duration / Policy.FINE_COUNTING_INTERVAL.getSeconds();
                request.increaseFine(numberOfIntervals * Policy.FINE_PER_INTERVAL);
            }

            Duration duration = Duration.between(request.getDueDate(), LocalDateTime.now());
            long numberOfIntervals = duration.getSeconds() / Policy.FINE_COUNTING_INTERVAL.getSeconds();

            request.setFine(numberOfIntervals * Policy.FINE_PER_INTERVAL);

            log.info("Expired request {}: update time is {}, total fine of request is {}",
                    request.getId(),
                    request.getUpdateTime(),
                    request.getFine()
            );
        });

        borrowingRequests.forEach(request -> {
            if (LocalDateTime.now().isEqual(request.getDueDate())) {
                request.setStatus(this.EXPIRED_STATUS);
//                request.setFine(Policy.FINE_PER_INTERVAL);
            } else if (LocalDateTime.now().isAfter(request.getDueDate())) {
                request.setStatus(this.EXPIRED_STATUS);
//
//                Duration duration = Duration.between(request.getDueDate(), LocalDateTime.now());
//                long numberOfIntervals = duration.getSeconds() / Policy.FINE_COUNTING_INTERVAL.getSeconds();
//
//                request.setFine((numberOfIntervals + 1) * Policy.FINE_PER_INTERVAL);
            }
        });
    }

    @Override
    public ResponseEntity<?> delete(String identifiable) {
        Request request = this.find(identifiable);

        if (request == null) return ResponseEntity.noContent().build();

        this.requestRepository.delete(request);

        return ResponseEntity.ok(new RequestDto(request));
    }

    public Request find(String identifiable) {
        try {
            Request request = this.requestRepository.findById(Long.parseLong(identifiable)).orElse(null);
            if (request != null) return request;
        } catch (NumberFormatException ignored) {
        }

        return null;
    }

    private ResponseEntity<?> okResponse(Object body) {
        return ResponseEntity.ok(body);
    }
}
