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
public class LogoutRequest  {
    @Schema(description = "리프레시 토큰")
    @NotBlank(message = "Refresh Token Missing")
    private String refreshToken;
}
