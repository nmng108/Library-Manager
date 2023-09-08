package org.nmng.library.manager.dto.request;

import lombok.Data;
import org.nmng.library.manager.entity.User;

import java.util.List;

@Data
public class CreateUserDto {
    private String username;
    private String password;
    private String fullName;
    private String identity;
    private String phone;
    private String email;
    private String address;
    private List<String> roles;

    public User toUser() {
        User.UserBuilder userBuilder = User.builder()
                .username(this.username)
                .password(this.password)
                .fullName(this.fullName)
                .identityNumber(identity)
                .phone(this.phone);

        // optional fields
        if (this.email != null) userBuilder.email(this.email);
        if (this.address != null) userBuilder.address(this.address);

        return userBuilder.build();
    }

}
