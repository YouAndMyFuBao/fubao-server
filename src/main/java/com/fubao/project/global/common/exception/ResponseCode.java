package com.fubao.project.global.common.exception;

import com.sun.jdi.InternalException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;

@Getter
@RequiredArgsConstructor
public enum ResponseCode {
    //sucess
    OK("success",HttpStatus.OK,"요청에 성공하였습니다."),
    //member
    USER_NOT_FOUND("MEM-ERR-001", HttpStatus.NOT_FOUND, "존재하지 않는 유저입니다."),
    NOT_GET_KAKAO_INFO("MEM-ERR-002", HttpStatus.BAD_REQUEST, "카카오 정보를 가져오는데 실패했습니다."),
    POST_NOT_FOUND("MEM-ERR-003", HttpStatus.NOT_FOUND, "존재하지 않는 편지입니다."),
    //post
    FAILED_UPDATE_POST("POST-ERR-001", HttpStatus.INTERNAL_SERVER_ERROR, "업데이트에 실패했습니다."),
    PATCH_POST_CONTENT_NOT_EXIST("POST-ERR-002", HttpStatus.BAD_REQUEST, "변경할 게시물의 정보가 없습니다."),
    //GLOBAL
    BAD_REQUEST("GLB-ERR-001", HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    METHOD_NOT_ALLOWED("GLB-ERR-002", HttpStatus.METHOD_NOT_ALLOWED, "허용되지 않은 메서드입니다."),
    INTERNAL_SERVER_ERROR("GLB-ERR-003", HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 오류입니다."),
    //auth
    UNAUTHORIZED("AUTH-ERR-001", HttpStatus.UNAUTHORIZED, "접근 권한이 없는 유저입니다."),
    INVALID_TOKEN("AUTH-ERR-002", HttpStatus.UNAUTHORIZED, "유효한 토큰이 아닙니다."),
    TEST("TEST-ERR-001", HttpStatus.BAD_REQUEST, "테스트입니다");
    private final String code;
    private final HttpStatus status;
    private final String message;

    public String getMessage(Throwable e) {
        return this.getMessage(this.getMessage() + " - " + e.getMessage());
        // 결과 예시 - "Validation error - Reason why it isn't valid"
    }
    public String getMessage(String message) {
        return Optional.ofNullable(message)
                .filter(Predicate.not(String::isBlank))
                .orElse(this.getMessage());
    }
    public static ResponseCode valueOf(HttpStatus httpStatus) {
        if (httpStatus == null) {
            throw new InternalException("HttpStatus is null.");
        }

        return Arrays.stream(values())
                .filter(errorCode -> errorCode.getStatus() == httpStatus)
                .findFirst()
                .orElseGet(() -> {
                    if (httpStatus.is4xxClientError()) {
                        return ResponseCode.BAD_REQUEST;
                    } else if (httpStatus.is5xxServerError()) {
                        return ResponseCode.INTERNAL_SERVER_ERROR;
                    } else {
                        return ResponseCode.OK;
                    }
                });
    }
}
