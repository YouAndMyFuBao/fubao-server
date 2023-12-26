package com.fubao.project.global.common.constant;

import lombok.Getter;

@Getter
public enum State {
    A("ACTIVE"),
    D("DELETE");
    private final String name;

    State(String name) {
        this.name = name;
    }
}
