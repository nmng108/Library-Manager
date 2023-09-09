package org.nmng.library.manager.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users", schema = "library-manager")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class User {
    @Column(nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Size(min = 10, max = 15)
    private long id;

    @Column(nullable = false, unique = true, length = 16)
//    @Size(min = 5, max = 16)
    private String username;
    @Column(nullable = false, length = 32)
//    @Size(min = 3, max = 32)
    private String password;

    @Column(nullable = false, length = 70)
//    @Size(min = 3, max = 70)
    private String fullName;
    @Column(nullable = false, unique = true, length = 15)
//    @Size(min = 10, max = 15)
    private String identityNumber;
    @Column(nullable = false, length = 15)
//    @Size(min = 10, max = 15)
    private String phone;
    @Column(nullable = true, length = 90)
    private String email;
    @Column(nullable = true, length = 120)
    private String address;

    @OneToMany(mappedBy = "user")
    @JsonBackReference
    private List<UserRole> roles;

    @Column(nullable = false, insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createTime;
    @Column(nullable = false, insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updateTime;
}
