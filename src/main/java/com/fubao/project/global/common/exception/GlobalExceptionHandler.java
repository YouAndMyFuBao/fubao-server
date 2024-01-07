package com.fubao.project.global.common.exception;

import com.fubao.project.global.util.SlackWebhookUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;


@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {
    private final SlackWebhookUtil slackWebhookUtil;

    // 직접 정의한 에러
    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ErrorResponse> handleCustomException(final CustomException e, HttpServletRequest request) {
        log.error("handleCustomException: {}", e.getResponseCode().toString());
        final ErrorResponse errorResponse = ErrorResponse.of(e.getResponseCode());
        slackWebhookUtil.slackNotificationThread(e,request);
        return ResponseEntity
                .status(e.getResponseCode().getStatus())
                .body(errorResponse);
    }

    // 지원하지 않는 HttpRequestMethod
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

    //validation exception 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> processValidationError(MethodArgumentNotValidException e, HttpServletRequest request) {
        final ErrorResponse errorResponse = ErrorResponse.of(ResponseCode.INTERNAL_SERVER_ERROR, e);
        slackWebhookUtil.slackNotificationThread(e,request);
        return ResponseEntity
                .status(e.getStatusCode())
                .body(errorResponse);
    }

    //잘못된 자료형으로 인한 에러
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> methodArgumentTypeMismatchExceptionError(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        final ErrorResponse errorResponse = ErrorResponse.of(ResponseCode.BAD_REQUEST, e);
        slackWebhookUtil.slackNotificationThread(e,request);
        return ResponseEntity
                .status(ResponseCode.BAD_REQUEST.getStatus())
                .body(errorResponse);
    }

    //지원하지 않는 media type 에러
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> httpMediaTypeNotSupportedExceptionError(HttpMediaTypeNotSupportedException e, HttpServletRequest request) {
        final ErrorResponse errorResponse = ErrorResponse.of(ResponseCode.BAD_REQUEST, e);
        slackWebhookUtil.slackNotificationThread(e,request);
        return ResponseEntity
                .status(e.getStatusCode())
                .body(errorResponse);
    }

    //외부 api client 에러
    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ErrorResponse> httpMediaTypeNotSupportedExceptionError(HttpClientErrorException e, HttpServletRequest request) {
        final ErrorResponse errorResponse = ErrorResponse.of(ResponseCode.INTERNAL_SERVER_ERROR, e);
        slackWebhookUtil.slackNotificationThread(e,request);
        return ResponseEntity
                .status(e.getStatusCode())
                .body(errorResponse);
    }

    //외부 api server 에러
    @ExceptionHandler(HttpServerErrorException.class)
    public ResponseEntity<ErrorResponse> httpServerErrorExceptionError(HttpServerErrorException e, HttpServletRequest request) {
        final ErrorResponse errorResponse = ErrorResponse.of(ResponseCode.INTERNAL_SERVER_ERROR, e);
        slackWebhookUtil.slackNotificationThread(e,request);
        return ResponseEntity
                .status(e.getStatusCode())
                .body(errorResponse);
    }
}