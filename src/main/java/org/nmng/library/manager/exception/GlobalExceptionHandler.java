package org.nmng.library.manager.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.nmng.library.manager.dto.response.common.CommonResponse;
import org.nmng.library.manager.dto.response.common.FailureResponse;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.NoSuchFileException;
import java.util.HashMap;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * @param e UsernameNotFoundException
     * @return ResponseEntity with 403 HTTP status
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public CommonResponse handleUsernameNotFoundException(UsernameNotFoundException e) {
        this.logError(e);
        return new FailureResponse("User not found");
    }

    /**
     * @param e LockedException
     * @return ResponseEntity with 403 HTTP status
     */
    @ExceptionHandler(LockedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public CommonResponse handleAccountBeingLockedException(LockedException e) {
        this.logError(e);
        return new FailureResponse("User is locked");
    }

    /**
     * @param e NoSuchFileException
     * @return  400
     */
    @ExceptionHandler(NoSuchFileException.class)
    public ResponseEntity<CommonResponse> handleStoragePathNotFound(NoSuchFileException e) {
        this.logError(e);
        return new InvalidRequestException(e.getMessage()).toResponse();
    }

    /**
     * @param e PropertyReferenceException
     * @return 400
     */
    @ExceptionHandler(PropertyReferenceException.class)
    public ResponseEntity<CommonResponse> handleMismatchPropertyName(PropertyReferenceException e) {
        this.logError(e);
        return new InvalidRequestException(e.getMessage()).toResponse();
    }

//    @ExceptionHandler(InvalidDataAccessApiUsageException.class)
//    public ResponseEntity<CommonResponse> handleMismatchPropertyName(InvalidDataAccessApiUsageException e) {
//        log.error("InvalidDataAccessApiUsageException: {}", e.getMessage());
//        return this.handleInvalidRequest(new InvalidRequestException("Wrong field name"));
//    }

    /**
     * Thrown when request data violates the requirements defined with Jpa - Hibernate validation
     *
     * @param e BindException
     * @return 400
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<CommonResponse> handleValidationException(BindException e) {
        this.logError(e);
        // field_name => error_message
        return new InvalidRequestException(e.getFieldErrors().stream().collect(
                Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage, (a, b) -> b)
        )).toResponse();
    }

    /**
     * @param e ConstraintViolationException
     * @return 400
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<CommonResponse> handleUrlValidation(ConstraintViolationException e) {
        this.logError(e);

        HashMap<String, String> details = new HashMap<>();

        e.getConstraintViolations().forEach(ex -> {
            String[] fieldPath = ex.getPropertyPath().toString().split("\\.");
            if (fieldPath.length > 0) details.put(fieldPath[fieldPath.length - 1], e.getMessage());
        });

        return new InvalidRequestException(details).toResponse();
    }

    /**
     * An exception happens when request body format does not follow the standard (e.g. JSON)
     * or parser cannot parse Enum/Date fields, ....
     *
     * @param e HttpMessageNotReadableException
     * @return 400
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<CommonResponse> handleUnreadableRequest(HttpMessageNotReadableException e) {
        this.logError(e);
        return new InvalidRequestException("Wrong request body. Check and try again.").toResponse();
    }

    /**
     * @param e
     * @return
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<CommonResponse> handleNotFoundException(ResourceNotFoundException e) {
        this.logError(e);
        return e.toResponse();
    }

    /**
     * An exception caused by user's invalid request data; manually thrown in code
     *
     * @param e InvalidRequestException
     * @return 400
     */
    @ExceptionHandler({InvalidRequestException.class})
    public ResponseEntity<CommonResponse> handleInvalidRequest(InvalidRequestException e) {
        this.logError(e);
        return e.toResponse();
    }

    /**
     * A common Http exception thrown manually in code
     *
     * @param e
     * @return
     */
    @ExceptionHandler(HttpException.class)
    public ResponseEntity<CommonResponse> handleHttpException(HttpException e) {
        this.logError(e);
        return e.toResponse();
    }

    /**
     * Catch any exception other than the exceptions declared above.
     * <p>
     * By default, we consider all other exceptions are Internal server errors.
     *
     * @param e Exception
     * @return 500
     */
    @ExceptionHandler({Exception.class})
    public ResponseEntity<CommonResponse> handleCommonExceptions(Exception e) {
        this.logError(e);
        // trace error
        e.printStackTrace();

        return new HttpException().toResponse();
    }

    private void logError(Exception e) {
        log.error("{}: {}", e.getClass().getSimpleName(), e.getMessage());
    }
}
