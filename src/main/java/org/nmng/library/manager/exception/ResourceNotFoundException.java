package org.nmng.library.manager.exception;

public class ResourceNotFoundException extends HttpException {
    private static final int HTTP_STATUS_CODE = 404;
    private static final String ERROR_CODE = "E10";

    public ResourceNotFoundException() {
        super(HTTP_STATUS_CODE);
    }

    public ResourceNotFoundException(String message) {
        super(HTTP_STATUS_CODE, ERROR_CODE, message);
    }
}
