package com.fubao.project.global.common.exception;

import com.fubao.project.global.util.SlackWebhookUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {
    private final SlackWebhookUtil slackWebhookUtil;
    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ErrorResponse> handleCustomException(final CustomException e, HttpServletRequest request) {
        log.error("handleCustomException: {}", e.getResponseCode().toString());
        final ErrorResponse errorResponse = ErrorResponse.of(e.getResponseCode());
        slackWebhookUtil.slackNotificationThread(e,request);
        return ResponseEntity
                .status(e.getResponseCode().getStatus())
                .body(errorResponse);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(final HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        log.error("handleHttpRequestMethodNotSupportedException: {}", e.getMessage());
        final ErrorResponse errorResponse = ErrorResponse.of(ResponseCode.METHOD_NOT_ALLOWED);
        slackWebhookUtil.slackNotificationThread(e,request);
        return ResponseEntity
                .status(ResponseCode.METHOD_NOT_ALLOWED.getStatus())
                .body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(final Exception e, HttpServletRequest request) {
        log.error("handleException: {}", e.getMessage());
        final ErrorResponse errorResponse = ErrorResponse.of(ResponseCode.INTERNAL_SERVER_ERROR);
        slackWebhookUtil.slackNotificationThread(e,request);
        return ResponseEntity
                .status(ResponseCode.INTERNAL_SERVER_ERROR.getStatus())
                .body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> processValidationError(MethodArgumentNotValidException e, HttpServletRequest request) {
        final ErrorResponse errorResponse = ErrorResponse.of(ResponseCode.INTERNAL_SERVER_ERROR, e);
        slackWebhookUtil.slackNotificationThread(e,request);
        return ResponseEntity
                .status(e.getStatusCode())
                .body(errorResponse);
    }
}