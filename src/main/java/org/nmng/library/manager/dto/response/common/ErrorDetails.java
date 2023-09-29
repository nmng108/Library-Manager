package org.nmng.library.manager.dto.response.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public final class ErrorDetails {
    private String errorCode;
    private Object messages;
}
