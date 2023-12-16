package com.fubao.project.domain.api.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthTokens {
	@Schema(description = "access token")
	private String accessToken;
	@Schema(description = "refresh token")
	private String refreshToken;

	public static AuthTokens of(String accessToken, String refreshToken) {
		return new AuthTokens(accessToken, refreshToken);
	}
}
