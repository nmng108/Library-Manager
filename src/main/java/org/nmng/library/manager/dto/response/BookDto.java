package org.nmng.library.manager.dto.response;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import org.nmng.library.manager.entity.Book;
import org.nmng.library.manager.entity.Category;
import org.nmng.library.manager.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
public class BookDto {
    private int id;
    private String name;
    private Integer bookNumber;
    private String authors;
    private Integer edition;
    private String publisher;
    private Integer quantity;
    private String category;
    private LocalDateTime updateTime;

    public BookDto(Book book) {
        Objects.requireNonNull(book);

        this.name = book.getName();
        this.bookNumber = book.getBookNumber();
        this.authors = book.getAuthors();
        this.edition = book.getEdition();
        this.publisher = book.getPublisher();
        this.quantity = book.getQuantity();
        this.category = book.getCategory().getName();
        this.updateTime = book.getUpdateTime();
    }
}
