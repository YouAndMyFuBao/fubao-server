package com.fubao.project.domain.service.oauth.kakao;

import com.fubao.project.global.common.constant.OAuthProvider;
import com.fubao.project.global.common.exception.ResponseCode;
import com.fubao.project.global.common.exception.CustomException;
import com.fubao.project.global.common.oauth.OAuthApiClient;
import com.fubao.project.global.common.oauth.OAuthInfoResponse;
import com.fubao.project.global.common.oauth.OAuthLoginParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class KakaoApiClient implements OAuthApiClient {
    private static final String GRANT_TYPE = "authorization_code";

    @Value("${oauth.kakao.url.auth}")
    private String authUrl;

    @Value("${oauth.kakao.url.api}")
    private String apiUrl;

    @Value("${oauth.kakao.client-id}")
    private String clientId;
    @Value("${oauth.kakao.admin-key}")
    private String adminKey;
    private final RestTemplate restTemplate;

    @Override
    public OAuthProvider oAuthProvider() {
        return OAuthProvider.KAKAO;
    }

    @Override
    public String requestAccessToken(OAuthLoginParams params) {
        String url = authUrl + "/oauth/token";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = params.makeBody();
        body.add("grant_type", GRANT_TYPE);
        body.add("client_id", clientId);
        ResponseEntity<KakaoTokens> response;
        HttpEntity<?> request = new HttpEntity<>(body, httpHeaders);
        try {
            response = restTemplate.exchange(url, HttpMethod.POST, request, KakaoTokens.class);
        } catch (HttpClientErrorException e) {
            throw new CustomException(ResponseCode.NOT_GET_KAKAO_INFO);
        }

        if (response.getBody() == null) {
            throw new CustomException(ResponseCode.NOT_GET_KAKAO_INFO);
        }

        return response.getBody().getAccessToken();
    }

    @Override
    public OAuthInfoResponse requestOAuthInfo(String accessToken) {
        String url = apiUrl + "/v2/user/me";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.set("Authorization", "Bearer " + accessToken);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("property_keys", "[\"id\", \"kakao_account.\", \"properties.\", \"has_signed_up.\"]");

        HttpEntity<?> request = new HttpEntity<>(body, httpHeaders);

        return restTemplate.postForObject(url, request, KakaoInfoResponse.class);
    }

    @Override
    public void disconnect(String providerId) {
        String url = apiUrl + "/v1/user/unlink";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.set("Authorization", "KakaoAK " + adminKey);
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("target_id_type", "user_id");
        body.add("target_id", providerId);
        HttpEntity<?> request = new HttpEntity<>(body, httpHeaders);
        restTemplate.exchange(url, HttpMethod.POST, request, String.class);
    }
}
