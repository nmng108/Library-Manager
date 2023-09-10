package org.nmng.library.manager.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "requests", schema = "library-manager")
@NoArgsConstructor
@Data
public class Request {
    @Column(nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, columnDefinition = "DOUBLE UNSIGNED NOT NULL")
    private Double fine = 0.0;

    @Column(name = "due_date", nullable = false, updatable = false, columnDefinition = "DATETIME DEFAULT (CURRENT_TIMESTAMP + INTERVAL 5 MINUTE)")
    private LocalDateTime dueDate;

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne
    private User patron;

    @JoinColumn(name = "status_id", nullable = false)
    @ManyToOne
    private RequestStatus status;

    @OneToMany(mappedBy = "request")
    @JsonBackReference
    private List<RequestDetail> details;

    @Column(nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createTime; // this is also the start date of request
    @Column(nullable = false, insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updateTime;

    public boolean isBorrowing() {
        return this.status.getName().compareToIgnoreCase(RequestStatus.BORROWING) == 0;
    }

    public boolean hasExpired() {
        return this.status.getName().compareToIgnoreCase(RequestStatus.EXPIRED) == 0;
    }

    public void increaseFine(double value) {
        this.fine += value;
    }
}
