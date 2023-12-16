package com.fubao.project.domain.service.oauth.kakao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fubao.project.global.common.constant.OAuthProvider;
import com.fubao.project.global.common.oauth.OAuthInfoResponse;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoInfoResponse implements OAuthInfoResponse {
    @JsonIgnoreProperties(ignoreUnknown = true)
    private String id;

    @Override
    public String getProviderId() {
        return this.id;
    }

    @Override
    public OAuthProvider getOAuthProvider() {
        return OAuthProvider.KAKAO;
    }
}
