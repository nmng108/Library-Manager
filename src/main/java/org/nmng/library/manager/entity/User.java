package org.nmng.library.manager.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users", schema = "library-manager")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class User implements UserDetails {
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
    @ToString.Exclude
    private List<UserRole> roles;

    @Column(nullable = false, insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createTime;
    @Column(nullable = false, insertable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updateTime;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { // ?? how to get role?
        return this.roles.stream().map(userRole -> new SimpleGrantedAuthority(userRole.getRole().getName())).toList();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
