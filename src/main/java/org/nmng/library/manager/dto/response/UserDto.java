package org.nmng.library.manager.dto.response;

import lombok.Data;
import org.nmng.library.manager.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
public class UserDto {
    private String username;
    private String password;
    private List<String> roles;
    private String fullName;
    private String identity;
    private String phone;
    private String email;
    private String address;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public UserDto(User user) {
        Objects.requireNonNull(user);

        this.username = user.getUsername();
        this.password = user.getPassword();
        this.roles = user.getRoles().stream()
                .map(userRole -> userRole.getRole().getName())
                .collect(Collectors.toList());
        this.fullName = user.getFullName();
        this.identity = user.getIdentityNumber();
        this.phone = user.getPhone();
        this.email = user.getEmail();
        this.address = user.getAddress();
        this.createTime = user.getCreateTime();
        this.updateTime = user.getUpdateTime();
    }
}
