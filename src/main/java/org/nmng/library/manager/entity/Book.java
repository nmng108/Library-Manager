package org.nmng.library.manager.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "books", schema = "library-manager")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Book {
    @Column(nullable = false, insertable = false, updatable = false, columnDefinition = "INT UNSIGNED")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 45)
    private String name;
    @Column(nullable = true, columnDefinition = "TINYINT(2) UNSIGNED")
    private Integer bookNumber;
    @Column(nullable = false, length = 100)
    private String authors;
    @Column(nullable = false, columnDefinition = "SMALLINT(3) UNSIGNED DEFAULT 1")
    private Integer edition = 1;
    @Column(nullable = false, length = 60)
    private String publisher;
    @Column(nullable = false, columnDefinition = "MEDIUMINT(6) UNSIGNED")
    private Integer quantity;

    @JoinColumn
    @ManyToOne
    private Category category;

    @Column(nullable = false, insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createTime;
    @Column(nullable = false, insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updateTime;
}
