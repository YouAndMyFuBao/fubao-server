package com.fubao.project.global.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Optional;
import java.util.function.Predicate;

@Getter
@RequiredArgsConstructor
public enum ResponseCode {
    //success
    OK("success", HttpStatus.OK, "요청에 성공하였습니다."),
    //member
    USER_NOT_FOUND("MEM-ERR-001", HttpStatus.NOT_FOUND, "존재하지 않는 유저입니다."),
    NOT_GET_KAKAO_INFO("MEM-ERR-002", HttpStatus.BAD_REQUEST, "카카오 정보를 가져오는데 실패했습니다."),
    //post
    FAILED_UPDATE_POST("POST-ERR-001", HttpStatus.INTERNAL_SERVER_ERROR, "업데이트에 실패했습니다."),
    PATCH_POST_CONTENT_NOT_EXIST("POST-ERR-002", HttpStatus.BAD_REQUEST, "변경할 편지의 정보가 없습니다."),
    DO_NOT_PATCH_POST("POST_ERR_003", HttpStatus.FORBIDDEN, "본인이 작성한 편지만 수정할 수 있습니다."),
    POST_NOT_FOUND("POST-ERR-004", HttpStatus.NOT_FOUND, "존재하지 않는 편지입니다."),
    DO_NOT_DELETE_POST("POST-ERR-005", HttpStatus.FORBIDDEN, "본인이 작성한 편지만 삭제할 수 있습니다."),

    //GLOBAL
    BAD_REQUEST("GLB-ERR-001", HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    METHOD_NOT_ALLOWED("GLB-ERR-002", HttpStatus.METHOD_NOT_ALLOWED, "허용되지 않은 메서드입니다."),
    INTERNAL_SERVER_ERROR("GLB-ERR-003", HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 오류입니다."),
    //auth
    UNAUTHORIZED("AUTH-ERR-001", HttpStatus.UNAUTHORIZED, "접근 권한이 없는 유저입니다."),
    INVALID_TOKEN("AUTH-ERR-002", HttpStatus.UNAUTHORIZED, "유효한 토큰이 아닙니다."),
    EXPIRED_TOKEN("AUTH-ERR-003", HttpStatus.UNAUTHORIZED, "만료된 토근입니다."),
    UNSUPPORTED_TOKEN("AUTH-ERR-004",HttpStatus.UNAUTHORIZED , "지원하지 않는 토큰입니다."),
    //test
    TEST("TEST-ERR-001", HttpStatus.BAD_REQUEST, "테스트입니다")
    ;
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
}
