package org.nmng.library.manager.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "request_status", schema = "library-manager")
@NoArgsConstructor
@Data
public class RequestStatus {
    public static final String BORROWING = "BORROWING";
    public static final String RETURNED = "RETURNED";
    public static final String CANCELLED = "CANCELLED";
    public static final String EXPIRED = "EXPIRED";

    @Column(nullable = false, columnDefinition = "TINYINT(1) UNSIGNED")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 10)
    private String name;

    @OneToMany(mappedBy = "status")
    @JsonBackReference
    private List<Request> requests;

    public RequestStatus(String name) {
        this.name = name;
    }
}
