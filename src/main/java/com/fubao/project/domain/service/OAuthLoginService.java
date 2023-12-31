package com.fubao.project.domain.service;

import com.fubao.project.domain.api.auth.dto.request.LogoutRequest;
import com.fubao.project.domain.api.auth.dto.request.TokenRegenerateRequest;
import com.fubao.project.domain.api.auth.dto.response.AuthTokens;
import com.fubao.project.global.common.oauth.OAuthLoginParams;

import java.util.UUID;

public interface OAuthLoginService {
    AuthTokens login(OAuthLoginParams params);

    AuthTokens tokenRegenerate(TokenRegenerateRequest tokenRegenerateRequest);

    void logout(LogoutRequest logoutRequest);

    void deactivation(UUID memberId);
}
