package org.nmng.library.manager.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.nmng.library.manager.entity.User;

@Data
public class CreateLibrarianDto {
    @NotBlank
    @Pattern(regexp = "[a-zA-Z0-9]{3,16}")
    private String username;
    @NotBlank
    @Size(min = 3, max = 32)
    private String password;
    @NotBlank
    @Size(min = 5, max = 70)
    @Pattern(regexp = "[A-Za-z]+( [a-zA-Z]+)*")
    private String fullName;
    @NotBlank
    @Pattern(regexp = "[a-zA-Z0-9]{10,15}")
    private String identity;
    @NotBlank
    @Pattern(regexp = "(0|(\\+[0-9]{1,3}))[a-zA-Z0-9]{9,11}")
    private String phone;
    @Size(min = 5, max = 90)
    @Pattern(regexp = "[a-zA-Z0-9]{3,}([._-][a-zA-Z0-9]+){0,3}@[a-zA-Z0-9]+(\\.[a-zA-Z0-9]+)*")
    private String email;
    @Size(max = 120)
    @Pattern(regexp = "(([0-9]+([./][0-9]+))|([A-Za-z]{1,20}))( ?[,.-] ?(([0-9]+([./][0-9]+))|([A-Za-z]{1,20})))*")
    private String address;

    public User toUser() {
        User.UserBuilder userBuilder = User.builder()
                .username(this.username)
                .password(this.password)
                .fullName(this.fullName)
                .identityNumber(identity)
                .phone(this.phone);

        // optional attributes
        if (this.email != null) userBuilder.email(this.email);
        if (this.address != null) userBuilder.address(this.address);

        return userBuilder.build();
    }

}
