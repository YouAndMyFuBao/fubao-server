package com.fubao.project.global.common.oauth;


import com.fubao.project.global.common.constant.OAuthProvider;

public interface OAuthInfoResponse {
	String getProviderId();
	OAuthProvider getOAuthProvider();
}
