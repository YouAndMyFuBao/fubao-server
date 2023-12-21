package com.fubao.project.global.common.response;

import com.fubao.project.global.common.exception.ResponseCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class ResponseDto {
    private final Boolean isSuccess;
    private final String code;
    private final String message;

    public static ResponseDto of(Boolean success, ResponseCode code) {
        return new ResponseDto(success, code.getCode(), code.getMessage());
    }

    public static ResponseDto of(Boolean success, ResponseCode code, Exception e) {
        return new ResponseDto(success, code.getCode(), code.getMessage(e));
    }

    public static ResponseDto of(Boolean success, ResponseCode code, String message) {
        return new ResponseDto(success, code.getCode(), code.getMessage(message));
    }
}
