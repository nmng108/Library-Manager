package org.nmng.library.manager.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode(callSuper = true)
@Data
public class BookSearchDto extends SearchDto {
    private String id;
    private String name;
    private String category;
    private String bookNumber;
    private String authors;
    private String edition;
    private String publisher;
}
