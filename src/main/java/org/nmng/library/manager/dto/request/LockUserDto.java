package org.nmng.library.manager.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;

@Getter
@Setter
public class LockUserDto {
    @NotBlank
    private String identifiable;
    @NotNull
    private Boolean locked;
    @NotBlank
    @Pattern(regexp = "(FOREVER)|(([0-9]{1,3}DT)?([0-9]{1,2}H)?([0-9]{1,2}M)?([0-9]{1,2}S)?)")
    private String lastDuration;

    public Duration getLastDuration() {
        if (this.lastDuration.equalsIgnoreCase("FOREVER")) return Duration.ZERO;
        return Duration.parse("P" + lastDuration);
    }
}
