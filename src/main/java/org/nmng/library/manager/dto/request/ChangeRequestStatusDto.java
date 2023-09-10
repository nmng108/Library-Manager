package org.nmng.library.manager.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeRequestStatusDto {
    private String id;
    private String status;
}
