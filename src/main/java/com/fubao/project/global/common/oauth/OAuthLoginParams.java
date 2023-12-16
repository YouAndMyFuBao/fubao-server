package com.fubao.project.global.common.oauth;

import com.fubao.project.global.common.constant.OAuthProvider;

import org.springframework.util.MultiValueMap;

public interface OAuthLoginParams {
	OAuthProvider oAuthProvider();
	MultiValueMap<String, String> makeBody();
}
