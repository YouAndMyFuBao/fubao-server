package com.fubao.project.domain.service.oauth;

import com.fubao.project.global.common.constant.OAuthProvider;
import com.fubao.project.global.common.oauth.OAuthApiClient;
import com.fubao.project.global.common.oauth.OAuthInfoResponse;
import com.fubao.project.global.common.oauth.OAuthLoginParams;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class RequestOAuthInfoService {
    private final Map<OAuthProvider, OAuthApiClient> clients = new HashMap<>();

    public RequestOAuthInfoService(List<OAuthApiClient> clients) {
        for (OAuthApiClient client : clients) {
            this.clients.put(client.oAuthProvider(), client);
        }
    }

    public OAuthInfoResponse request(OAuthLoginParams params) {
        OAuthApiClient client = clients.get(params.oAuthProvider());

        String accessToken = client.requestAccessToken(params);
        return client.requestOAuthInfo(accessToken);
    }
}
