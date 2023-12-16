package com.fubao.project.global.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<String> handleCustomException(final CustomException e) {
        log.error("handleCustomException: {}", e.getResponseCode().toString());
        final ErrorDTO errorDTO = new ErrorDTO(e.getResponseCode());
        return ResponseEntity
                .status(e.getResponseCode().getStatus())
                .body(errorDTO.getMessage());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<String> handleHttpRequestMethodNotSupportedException(final HttpRequestMethodNotSupportedException e) {
        log.error("handleHttpRequestMethodNotSupportedException: {}", e.getMessage());
        final ErrorDTO errorDTO = new ErrorDTO(CustomErrorCode.METHOD_NOT_ALLOWED);
        return ResponseEntity
                .status(errorDTO.getHttpStatus())
                .body(errorDTO.getMessage());
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<String> handleException(final Exception e) {
        log.error("handleException: {}", e.getMessage());
        final ErrorDTO errorDTO = new ErrorDTO(CustomErrorCode.INTERNAL_SERVER_ERROR);
        return ResponseEntity
                .status(errorDTO.getHttpStatus())
                .body(errorDTO.getMessage());
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> processValidationError(MethodArgumentNotValidException ex) {
        return ResponseEntity
                .status(ex.getStatusCode())
                .body(ex.getBindingResult().getFieldError().getDefaultMessage());
    }
}