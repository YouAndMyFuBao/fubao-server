package com.fubao.project.domain.api.auth.dto.request;

import com.fubao.project.global.common.constant.OAuthProvider;
import com.fubao.project.global.common.oauth.OAuthLoginParams;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
@Getter
@NoArgsConstructor
public class KakaoLoginRequest implements OAuthLoginParams {
    @Schema(description = "인가코드")
    @NotBlank(message = "Authorization Code Missing")
    private String authorizationCode;

    @Override
    public OAuthProvider oAuthProvider() {
        return OAuthProvider.KAKAO;
    }

    @Override
    public MultiValueMap<String, String> makeBody() {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", authorizationCode);
        return body;
    }
}
