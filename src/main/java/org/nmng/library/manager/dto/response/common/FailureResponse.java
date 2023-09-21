package org.nmng.library.manager.dto.response.common;

import lombok.Getter;
import lombok.Setter;
import org.nmng.library.manager.dto.response.SuccessState;

@Getter
@Setter
public class FailureResponse extends CommonResponse {
    private ErrorDetails errors;

    public FailureResponse(String errorCode, String messages) {
        super(SuccessState.FALSE);
        this.errors = new ErrorDetails(errorCode, messages);
    }

    public FailureResponse(ErrorDetails errorDetails) {
        super(SuccessState.FALSE);
        this.errors = errorDetails;
    }
}
