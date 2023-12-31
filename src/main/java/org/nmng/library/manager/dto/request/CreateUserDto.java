package org.nmng.library.manager.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.nmng.library.manager.entity.Role;
import org.nmng.library.manager.entity.User;
import org.nmng.library.manager.validator.AcceptedStrings;

import java.util.List;

@Data
public class CreateUserDto {
    @NotBlank
    @Pattern(regexp = "[a-zA-Z0-9]{3,16}")
    private String username;
    @NotBlank
    @Size(min = 3, max = 32)
    private String password;
    @NotBlank
    @Pattern(regexp = "[A-Za-z]{1,10}( [a-zA-Z]{1,10})*")
    private String fullName;
    @NotBlank
    @Pattern(regexp = "[a-zA-Z0-9]{10,15}")
    private String identity;
    @NotBlank
    @Pattern(regexp = "[0-9]{10,15}")
    private String phone;
    @Pattern(regexp = "[a-zA-Z0-9]+([._-][a-zA-Z0-9]+){0,3}@[a-zA-Z0-9](\\.[a-zA-Z0-9])*")
    private String email;
    @Pattern(regexp = "(([0-9]+([./][0-9]+))|([A-Za-z]{1,20}))( ?[,.-] ?(([0-9]+([./][0-9]+))|([A-Za-z]{1,20})))*")
    private String address;
    @NotEmpty
    @AcceptedStrings({Role.ADMIN, Role.LIBRARIAN, Role.PATRON})
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
