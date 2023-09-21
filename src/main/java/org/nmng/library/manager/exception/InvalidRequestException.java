package org.nmng.library.manager.exception;

import java.util.Map;

public class InvalidRequestException extends HttpException {
    private static final int HTTP_STATUS_CODE = 400;
    private static final String ERROR_CODE = "E00";

    public InvalidRequestException(String message) {
        super(HTTP_STATUS_CODE, ERROR_CODE, message);
    }

    public InvalidRequestException(Map<String, String> messages) {
        super(HTTP_STATUS_CODE, ERROR_CODE, messages);
    }

//    public InvalidRequestException(List<FieldError> fieldExceptions) {
//        super(HTTP_STATUS_CODE, ERROR_CODE, fieldExceptions.stream().collect(
//                Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage, (a, b) -> b)
//        ));
//    }
//
//    public ResponseEntity<CommonResponse> toResponse() {
//        return ResponseEntity.badRequest().body(
//                CommonResponse.builder().success(SuccessState.FALSE).errors(this.errorDetails).build()
//        );
//    }
}
