package org.nmng.library.manager.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.nmng.library.manager.entity.Book;

@Data
public class UpdateBookDto {
    @NotNull
    @Min(1)
    private Integer id;
    @Pattern(regexp = "[A-Za-z0-9]{1,10}( [a-zA-Z0-9]{1,10})*")
    private String name;
    @Min(1)
    private Integer bookNumber; // optional
    @Pattern(regexp = "[A-Za-z]{1,10}( [a-zA-Z]{1,10})*,( [A-Za-z]{1,10}( [a-zA-Z]{1,10})*)*")
    private String authors;
    @Min(1)
    private Integer edition; // optional
    @Pattern(regexp = "[A-Za-z0-9]{1,10}( [a-zA-Z0-9]{1,10})*")
    private String publisher;
    @Pattern(regexp = "[A-Za-z0-9]{1,10}( [a-zA-Z0-9]{1,10})*")
    private String mainCategory;
    @Min(0)
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
