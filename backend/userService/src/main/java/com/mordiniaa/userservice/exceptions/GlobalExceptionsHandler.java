package com.mordiniaa.userservice.exceptions;

import com.mordiniaa.userservice.responses.APIExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionsHandler {

    @ExceptionHandler({
            BadRequestException.class,
            UsersNotAvailableException.class
    })
    public ResponseEntity<APIExceptionResponse> unsupportedOperation(RuntimeException e) {
        String message = e.getMessage();
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return exceptionResponse(message, status);
    }

    @ExceptionHandler(ArgumentNotPresentException.class)
    public ResponseEntity<APIExceptionResponse> argumentNotPresentException(ArgumentNotPresentException e) {
        String message = e.getMessage();
        int status = e.getStatus();
        return exceptionResponse(message, status);
    }

    @ExceptionHandler({
            RoleNotFoundException.class
    })
    public ResponseEntity<APIExceptionResponse> notFoundException(RuntimeException e) {
        String message = e.getMessage();
        HttpStatus status = HttpStatus.NOT_FOUND;
        return exceptionResponse(message, status);
    }

    @ExceptionHandler(AddressValidationException.class)
    public ResponseEntity<APIExceptionResponse> addressValidationException(AddressValidationException e) {
        String message = e.getMessage();
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return exceptionResponse(message, status);
    }

    @ExceptionHandler(ContactDataValidationException.class)
    public ResponseEntity<APIExceptionResponse> contactDataValidationException(ContactDataValidationException e) {
        String message = e.getMessage();
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return exceptionResponse(message, status);
    }

    private ResponseEntity<APIExceptionResponse> exceptionResponse(String message, int status) {
        return exceptionResponse(message, HttpStatus.valueOf(status));
    }

    private ResponseEntity<APIExceptionResponse> exceptionResponse(String message, HttpStatus status) {
        return new ResponseEntity<>(
                new APIExceptionResponse(
                        status.value(),
                        message
                ),
                status
        );
    }
}
