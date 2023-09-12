package org.nmng.library.manager.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Immutable;

import java.time.LocalDateTime;

@Entity
@Table(name = "book_category_borrow_time", schema = "library-manager")
@Immutable
@NoArgsConstructor
@Getter
@Setter
public class BookView {
    @Id
    private int id;
    private String name;
    private String category;
    private Integer bookNumber;
    private String authors;
    private Integer edition;
    private String publisher;
    private Integer availableQuantity;
    private Integer borrowTime;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
