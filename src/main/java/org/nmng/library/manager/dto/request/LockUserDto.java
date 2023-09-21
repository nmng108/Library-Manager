package org.nmng.library.manager.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;

@Setter
public class LockUserDto {
    @Getter
    @NotBlank
    @Size(min = 3, max = 16)
    private String identifiable;
    @NotNull
    private Boolean locked;
    @NotBlank
    @Pattern(regexp = "(([0-9]{1,3}D)?(T([0-9]{1,2}H)?([0-9]{1,2}M)?([0-9]{1,2}S)?))")
    private String lastDuration;

    public boolean isLocked() {
        return this.locked;
    }

    public Duration getLastDuration() {
//        System.out.println("LastDuration: %s".formatted(this.lastDuration));
        if (this.lastDuration.equalsIgnoreCase("FOREVER")) return Duration.ZERO;
        return Duration.parse("P" + lastDuration);
    }
}
