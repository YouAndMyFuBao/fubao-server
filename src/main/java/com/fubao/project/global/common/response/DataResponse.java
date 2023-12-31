package com.fubao.project.global.common.response;

import com.fubao.project.global.common.api.CustomResponseCode;
import com.fubao.project.global.common.exception.ResponseCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
public class DataResponse<T> extends ResponseDto {

    private final Object data;

    private DataResponse(Object data) {
        super(true, ResponseCode.OK.getCode(), ResponseCode.OK.getMessage());
        this.data = data;
    }

    private DataResponse(Object data, String message) {
        super(true, ResponseCode.OK.getCode(), message);
        this.data = data;
    }

    public static <T> DataResponse<T> of(T data) {
        if (data instanceof CustomResponseCode) {
            return new DataResponse<>(new MessageResponse(((CustomResponseCode) data).getMessage()));
        } else if (data instanceof String) {
            return new DataResponse<>(new MessageResponse((String) data));
        } else {
            return new DataResponse<>(data);
        }
    }

    @Getter
    private static class MessageResponse {

        private final String message;

        private MessageResponse(String message) {
            this.message = message;
        }
    }
}
