package com.fubao.project.global.common.constant;

import lombok.Getter;

@Getter
public enum MemberRole {
    ADMIN("관리자"),
    MEMBER("멤버");
    private final String name;

    MemberRole(String name) {
        this.name = name;
    }
}
