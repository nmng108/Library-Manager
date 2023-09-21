package org.nmng.library.manager.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDto {
    @NotBlank
    @Size(min = 3, max = 16)
    private String username;
    @NotBlank
    @Size(min = 3, max = 32)
    private String password;
}
