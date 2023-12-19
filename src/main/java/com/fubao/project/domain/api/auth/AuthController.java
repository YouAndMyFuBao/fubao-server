package com.fubao.project.domain.api.auth;

import com.fubao.project.domain.api.auth.dto.request.KakaoLoginRequest;
import com.fubao.project.domain.api.auth.dto.request.TokenRegenerateRequest;
import com.fubao.project.domain.api.auth.dto.response.AuthTokens;
import com.fubao.project.domain.service.OAuthLoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "소셜 인증", description = "소셜 API 명세서")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final OAuthLoginService oAuthLoginService;

    @Operation(summary = "카카오 로그인/회원가입")
    @PostMapping("/kakao")
    public ResponseEntity<AuthTokens> loginKakao(@Validated @RequestBody KakaoLoginRequest request) {
        return ResponseEntity.ok(oAuthLoginService.login(request));
    }

    @Operation(summary = "token 재발급")
    @PostMapping("/refresh")
    public ResponseEntity<AuthTokens> tokenRegenerate(@Validated @RequestBody TokenRegenerateRequest tokenRegenerateRequest) {
        return ResponseEntity.ok(oAuthLoginService.tokenRegenerate(tokenRegenerateRequest));
    }
}
