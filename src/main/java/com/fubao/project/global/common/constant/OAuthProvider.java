package com.fubao.project.global.common.constant;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OAuthProvider {
	KAKAO("KAKAO");
	private final String name;

	@JsonValue
	public String getValue() {
		return this.name();
	}
}
