package com.fubao.project.global.common.api;

import com.fubao.project.global.common.response.DataResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CustomResponseCode {
    MEMBER_SIGNUP("회원가입을 성공 하였습니다."),
    MEMBER_LOGOUT("로그아웃하였습니다."),
    MEMBER_DEACTIVATION("회원 탈퇴하였습니다."),
    POST_DELETE ("편지가 삭제되었습니다."),
    FUBAO_LOVE_ADD("푸바오에게 사랑을 보냈어요");
    private final String message;
}
