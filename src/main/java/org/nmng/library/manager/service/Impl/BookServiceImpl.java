package org.nmng.library.manager.service.Impl;

import org.nmng.library.manager.dao.BookViewRepository;
import org.nmng.library.manager.dao.CategoryRepository;
import org.nmng.library.manager.dao.BookRepository;
import org.nmng.library.manager.dto.request.BookSearchDto;
import org.nmng.library.manager.dto.request.CreateBookDto;
import org.nmng.library.manager.dto.response.PaginationSuccessResponse;
import org.nmng.library.manager.dto.response.common.SuccessResponse;
import org.nmng.library.manager.entity.Book;
import org.nmng.library.manager.entity.BookView;
import org.nmng.library.manager.entity.Category;
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
                ? (dto.getCount().equals(true) ? this.bookViewRepository.countByCriteria(bookSearch) : null)
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
            return ResponseEntity.badRequest().body("category not found");
        }

        if (this.bookRepository.hasExisted(book.getName(), book.getBookNumber(), book.getAuthors())) {
            return ResponseEntity.badRequest().body("has existed");
        }

        book.setCategory(category);
        book = this.bookRepository.save(book);

        return ResponseEntity.ok(book);
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
