package org.nmng.library.manager.dto.request;

import lombok.Data;
import org.nmng.library.manager.entity.Book;

@Data
public class CreateBookDto {
    private String name;
    private Integer bookNumber;
    private String authors;
    private Integer edition; // optional
    private String publisher;
    private String mainCategory;
    private Integer quantity;

    public Book toBook() {
        Book book = new Book();
        book.setName(this.name);
        book.setBookNumber(this.bookNumber);
        book.setAuthors(this.authors);
        book.setPublisher(this.publisher);
        book.setQuantity(this.quantity);

        if (this.edition != null) book.setEdition(this.edition);

        return book;
    }
}
