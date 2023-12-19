package com.fubao.project.domain.api.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TokenRegenerateRequest {
    @Schema(description = "엑세스 토큰")
    @NotBlank(message = "Access Token Missing")
    private String accessToken;

    @Schema(description = "리프레시 토큰")
    @NotBlank(message = "Refresh Token Missing")
    private String refreshToken;
}
