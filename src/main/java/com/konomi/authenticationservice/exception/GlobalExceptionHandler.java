package com.konomi.authenticationservice.exception;

import com.konomi.authenticationservice.dto.ApiErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collections;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiErrorDto> handleBadRequestException(BadRequestException ex) {
        ApiErrorDto apiError = new ApiErrorDto(HttpStatus.BAD_REQUEST, "BAD_REQUEST", Collections.singletonList(ex.getMessage()));
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiErrorDto> handleConflictException(ConflictException ex) {
        ApiErrorDto apiError = new ApiErrorDto(HttpStatus.CONFLICT, "CONFLICT", Collections.singletonList(ex.getMessage()));
        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorDto> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ApiErrorDto apiError = new ApiErrorDto(HttpStatus.NOT_FOUND, "NOT_FOUND", Collections.singletonList(ex.getMessage()));
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }
}
