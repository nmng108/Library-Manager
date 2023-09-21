package org.nmng.library.manager.service.Impl;

import org.nmng.library.manager.dao.BookViewRepository;
import org.nmng.library.manager.dao.CategoryRepository;
import org.nmng.library.manager.dao.BookRepository;
import org.nmng.library.manager.dto.request.BookSearchDto;
import org.nmng.library.manager.dto.request.CreateBookDto;
import org.nmng.library.manager.dto.request.UpdateBookDto;
import org.nmng.library.manager.dto.response.PaginationSuccessResponse;
import org.nmng.library.manager.dto.response.common.SuccessResponse;
import org.nmng.library.manager.entity.Book;
import org.nmng.library.manager.entity.BookView;
import org.nmng.library.manager.entity.Category;
import org.nmng.library.manager.exception.InvalidRequestException;
import org.nmng.library.manager.exception.ResourceNotFoundException;
import org.nmng.library.manager.model.BookSearchModel;
import org.nmng.library.manager.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookViewRepository bookViewRepository;
    private final CategoryRepository categoryRepository;

    public BookServiceImpl(BookRepository bookRepository, BookViewRepository bookViewRepository, CategoryRepository categoryRepository) {
        this.bookRepository = bookRepository;
        this.bookViewRepository = bookViewRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public ResponseEntity<?> getAll(BookSearchDto dto) {
        BookSearchModel bookSearch = new BookSearchModel(dto);
        Long counter = dto.getCount() != null
                ? (dto.getCount().equals("true") || dto.getCount().isEmpty()
                    ? this.bookViewRepository.countByCriteria(bookSearch) : null)
                : null;

        List<BookView> result = this.bookViewRepository.findByCriteria(bookSearch);

        return ResponseEntity.ok(dto.getPage() != null
                ? new PaginationSuccessResponse<>(true, result, counter, (long) dto.getSize())
                : new SuccessResponse(result)
        );

//        return ResponseEntity.ok(this.bookRepository.findAll());
    }

    @Override
    public ResponseEntity<?> getSpecifiedOne(String identifiable) {
        Book book = this.findBook(identifiable);

        return book == null ?
                ResponseEntity.notFound().build()
                :
                ResponseEntity.ok(book);
    }

    @Override
    public ResponseEntity<?> create(CreateBookDto dto) {
        Book book = dto.toBook();
        Category category = this.categoryRepository.findByNameIgnoreCase(dto.getMainCategory()).orElse(null);

        if (category == null) {
            throw new InvalidRequestException("category not found");
        }

        if (this.bookRepository.hasExisted(book.getName(), book.getBookNumber(), book.getAuthors())) {
            throw new InvalidRequestException("has existed");
        }

        book.setCategory(category);
        book = this.bookRepository.save(book);

        return ResponseEntity.ok(book);
    }

    @Override
    public ResponseEntity<?> update(UpdateBookDto dto) {
        Book book = this.findBook(String.valueOf(dto.getId()));

        if (book == null) throw new ResourceNotFoundException("Book not found");

        if (dto.getMainCategory() != null) {
            Category category = this.categoryRepository.findByNameIgnoreCase(dto.getMainCategory()).orElse(null);

            if (category == null) throw new ResourceNotFoundException("Category not found");
            book.setCategory(category);
        }
        if (dto.getName() != null) book.setName(dto.getName());
        if (dto.getBookNumber() != null) book.setBookNumber(dto.getBookNumber());
        if (dto.getAuthors() != null) book.setAuthors(dto.getAuthors());
        if (dto.getEdition() != null) book.setEdition(dto.getEdition());
        if (dto.getPublisher() != null) book.setPublisher(dto.getPublisher());
        if (dto.getQuantity() != null) book.setQuantity(dto.getQuantity());

        book = this.bookRepository.save(book);

        return ResponseEntity.ok(new SuccessResponse(book));
    }

    @Override
    public ResponseEntity<?> delete(String identifiable) {
        Book book = this.findBook(identifiable);

        if (book != null) this.bookRepository.delete(book);
        else return ResponseEntity.noContent().build();

        return ResponseEntity.ok(book);
    }

    /**
     *
     * @param identifiable includes id
     * @return
     */
    public Book findBook(String identifiable) {
        try {
            return this.bookRepository.findById(Integer.parseInt(identifiable)).orElse(null);
        } catch (NumberFormatException ignored) {}

        return null;
//        throw new RuntimeException("Book not found");
    }
}
