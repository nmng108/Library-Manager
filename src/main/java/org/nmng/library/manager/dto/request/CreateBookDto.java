package org.nmng.library.manager.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.nmng.library.manager.entity.Book;

@Data
public class CreateBookDto {
    @NotBlank
    @Size(min = 6, max = 45)
    @Pattern(regexp = "[A-Za-z0-9]{1,10}( [a-zA-Z0-9]{1,10})*")
    private String name;
    @Min(1)
    @Max(99)
    private Integer bookNumber; // optional
    @NotBlank
    @Size(max = 150)
    @Pattern(regexp = "([A-Za-z]{1,10}( [a-zA-Z]{1,10})*)([,;-]( [A-Za-z]{1,10}( [a-zA-Z]{1,10})*))*")
    private String authors;
    @Min(1)
    @Max(99)
    private Integer edition; // optional
    @NotBlank
    @Size(min = 3, max = 80)
    @Pattern(regexp = "[A-Za-z0-9]{1,10}( [a-zA-Z0-9]{1,10})*")
    private String publisher;
    @NotBlank
    @Pattern(regexp = "[A-Za-z0-9]{1,10}( [a-zA-Z0-9]{1,10})*")
    private String mainCategory;
    @NotNull
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
