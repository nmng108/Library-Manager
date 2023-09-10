package org.nmng.library.manager.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "request_details", schema = "library-manager")
@NoArgsConstructor
@Data
public class RequestDetail {
    @Column(nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JoinColumn(name = "request_id", nullable = false)
    @ManyToOne
    private Request request;

    @JoinColumn(name = "book_id", nullable = false)
    @ManyToOne
    private Book book;

    @Column(nullable = false, columnDefinition = "MEDIUMINT UNSIGNED NOT NULL DEFAULT 1")
    private Integer quantity = 1;

    @Column(nullable = false, insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createTime;
    @Column(nullable = false, insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updateTime;

    public RequestDetail(Request request, Book book, Integer quantity) {
        this.request = request;
        this.book = book;
        this.quantity = quantity != null ? quantity : this.quantity;
    }
}
