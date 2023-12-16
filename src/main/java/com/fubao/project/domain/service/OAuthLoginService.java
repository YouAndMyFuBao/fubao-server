package com.fubao.project.domain.service;

import com.fubao.project.domain.api.auth.dto.response.AuthTokens;
import com.fubao.project.global.common.oauth.OAuthLoginParams;

public interface OAuthLoginService {
    AuthTokens login(OAuthLoginParams params);
}
