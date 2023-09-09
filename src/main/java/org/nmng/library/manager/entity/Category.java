package org.nmng.library.manager.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "categories", schema = "library-manager")
@NoArgsConstructor
@Getter
@Setter
public class Category {
    @Column(nullable = false, columnDefinition = "SMALLINT(3) UNSIGNED")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true, length = 20)
    private String name;

    @OneToMany(mappedBy = "category")
    @JsonBackReference
    List<Book> books;

    @Column(nullable = false, insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createTime;
    @Column(nullable = false, insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updateTime;

    public Category(String name) {
        this.name = name;
    }
}
