package org.nmng.library.manager.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.nmng.library.manager.entity.User;

import java.util.List;

@Data
public class CreateUserDto {
    @Pattern(regexp = "[a-zA-Z0-9]{5,16}")
    private String username;
    @Size(min = 3, max = 32)
    private String password;
    @Pattern(regexp = "[A-Za-z]{1,10}( [a-zA-Z]{1,10})*")
    private String fullName;
    @Pattern(regexp = "[a-zA-Z0-9]{10,15}")
    private String identity;
    @Pattern(regexp = "[0-9]{10,15}")
    private String phone;
    private String email;
    private String address;
    @NotBlank
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
