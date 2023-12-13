package com.fubao.project.global.common.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ErrorDTO {
    private final HttpStatus httpStatus;
    private final String message;

    public ErrorDTO(CustomException e) {
        this.httpStatus = e.getResponseCode().getStatus();
        this.message = e.getMessage();
    }

    public ErrorDTO(CustomErrorCode customErrorCode) {
        this.httpStatus = customErrorCode.getStatus();
        this.message =  customErrorCode.getMessage();
    }
}