package com.fubao.project.global.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CustomErrorCode {
    //member
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 유저입니다."),
    NOT_GET_KAKAO_INFO(HttpStatus.BAD_REQUEST, "카카오 정보를 가져오는데 실패했습니다."),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 편지입니다."),
    //GLOBAL
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "허용되지 않은 메서드입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 오류입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "접근 권한이 없는 유저입니다."),

    //TEST

    TEST(HttpStatus.OK, "TEST 입니다"),
    FAILED_UPDATE_POST(HttpStatus.INTERNAL_SERVER_ERROR, "업데이트에 실패했습니다."),
    PATCH_POST_CONTENT_NOT_EXIST(HttpStatus.BAD_REQUEST, "변경할 게시물의 정보가 없습니다.");
    NOT_GET_KAKAO_INFO(HttpStatus.BAD_REQUEST, "카카오 정보를 가져오는데 실패했습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효한 토큰이 아닙니다.");
    private final HttpStatus status;
    private final String message;
}
