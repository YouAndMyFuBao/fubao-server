package com.fubao.project.global.common.oauth;

import com.fubao.project.global.common.constant.OAuthProvider;

public interface OAuthApiClient {
	OAuthProvider oAuthProvider();
	String requestAccessToken(OAuthLoginParams params);
	OAuthInfoResponse requestOAuthInfo(String accessToken);

	void disconnect(String providerId);
}
