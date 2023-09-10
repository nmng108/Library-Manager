package org.nmng.library.manager.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.nmng.library.manager.entity.Request;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class CreateRequestDto {
    @NotBlank
    private String user;
    private LocalDateTime createTime;
    private LocalDateTime dueDate;
    @NotEmpty
    private List<RequestDetailDto> books;

    @Getter
    @Setter
    public static class RequestDetailDto {
        @NotBlank
        private String id;
        @NotBlank
        @Min(1)
        @Max(99)
        private Integer quantity;
    }

    public LocalDateTime getCreateTime() {
        if (this.createTime == null) return LocalDateTime.now();
        if (this.createTime.isAfter(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(400), "invalid createTime");
        }

        return this.createTime;
    }

    public Request toBaseRequest() {
        this.createTime = this.getCreateTime();

        if (this.dueDate.isBefore(this.createTime) || this.dueDate.equals(this.createTime)) {
            throw new RuntimeException("invalid due date");
        }

        Request request = new Request();
        request.setDueDate(this.dueDate);
        request.setCreateTime(this.createTime);

        return request;
    }
}
