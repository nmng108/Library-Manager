package org.nmng.library.manager.exception;


import org.nmng.library.manager.dto.response.common.CommonResponse;
import org.nmng.library.manager.dto.response.common.ErrorDetails;
import org.nmng.library.manager.dto.response.common.FailureResponse;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public class HttpException extends RuntimeException {
    protected int httpStatusCode = 500;
    protected ErrorDetails errorDetails = null;

    // response without body
    public HttpException() {
        super();
    }
    // response without body

    public HttpException(Throwable throwable) {
        super(throwable);
    }

    // response without body
    public HttpException(String responseMessage, Throwable throwable) {
        super(responseMessage, throwable);
    }

    // response without body
    public HttpException(int httpStatusCode) {
        super();

        if (httpStatusCode < 400 || httpStatusCode >= 600) {
            throw new RuntimeException("Wrong error status code.");
        }

        this.httpStatusCode = httpStatusCode;
    }

    // response without body
    public HttpException(int httpStatusCode, Throwable throwable) {
        super(throwable);

        if (httpStatusCode < 400 || httpStatusCode >= 600) {
            throw new RuntimeException("Wrong error status code.");
        }

        this.httpStatusCode = httpStatusCode;
    }

    public HttpException(int httpStatusCode, String errorCode) {
        this(httpStatusCode);
        this.errorDetails = new ErrorDetails(errorCode, null);
    }

    public HttpException(int httpStatusCode, String errorCode, String message) {
        this(httpStatusCode, errorCode);
        this.errorDetails.setMessages(message);
    }

    public HttpException(int httpStatusCode, String errorCode, Map<String, String> messages) {
        this(httpStatusCode, errorCode);
        this.errorDetails.setMessages(messages);
    }

    public ResponseEntity<CommonResponse> toResponse() {
        return ResponseEntity.status(this.httpStatusCode).body(
                this.errorDetails == null
                        ? null
                        : new FailureResponse(this.errorDetails)
        );
    }
}
