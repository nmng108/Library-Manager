package org.nmng.library.manager.dto.response;

import lombok.Getter;
import lombok.Setter;
import org.nmng.library.manager.entity.Request;
import org.nmng.library.manager.entity.RequestDetail;
import org.nmng.library.manager.entity.User;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class RequestDto {
    private long id;
    private PatronDto patron;
    private String status;
    private Double fine;
    private LocalDateTime dueDate;
    private List<RequestDetailDto> details;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    @Getter
    @Setter
    public static class PatronDto {
        private Long id;
        private String username;
        private String fullName;

        public PatronDto(User user) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.fullName = user.getFullName();
        }
    }

    @Getter
    @Setter
    public static class RequestDetailDto {
        private String book;
        private Integer quantity;

        public RequestDetailDto(RequestDetail requestDetail) {
            this.book = requestDetail.getBook().getName();
            this.quantity = requestDetail.getQuantity();
        }
    }

    public RequestDto(Request request) {
        this.id = request.getId();
        this.patron = new PatronDto(request.getPatron());
        this.status = request.getStatus().getName();
        this.fine = request.getFine();
        this.dueDate = request.getDueDate();
        this.details = request.getDetails().stream().map(RequestDetailDto::new).toList();
        this.createTime = request.getCreateTime();
        this.updateTime = request.getUpdateTime();
    }
}
