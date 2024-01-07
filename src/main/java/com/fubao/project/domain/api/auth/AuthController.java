package com.fubao.project.domain.api.auth;

import com.fubao.project.domain.api.auth.dto.request.KakaoLoginRequest;
import com.fubao.project.domain.api.auth.dto.request.LogoutRequest;
import com.fubao.project.domain.api.auth.dto.request.TokenRegenerateRequest;
import com.fubao.project.domain.api.auth.dto.response.AuthTokens;
import com.fubao.project.domain.service.OAuthLoginService;
import com.fubao.project.global.common.api.CustomResponseCode;
import com.fubao.project.global.common.exception.CustomException;
import com.fubao.project.global.common.exception.ResponseCode;
import com.fubao.project.global.common.response.DataResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "소셜 인증", description = "소셜 API 명세서")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final OAuthLoginService oAuthLoginService;

    @Operation(summary = "카카오 로그인/회원가입")
    @PostMapping("/kakao")
    public ResponseEntity<DataResponse<AuthTokens>> loginKakao(@Validated @RequestBody KakaoLoginRequest request) {
        return ResponseEntity.ok(DataResponse.of(oAuthLoginService.login(request)));
    }

    @Operation(summary = "token 재발급")
    @PostMapping("/refresh")
    public ResponseEntity<DataResponse<AuthTokens>> tokenRegenerate(@Validated @RequestBody TokenRegenerateRequest tokenRegenerateRequest) {
        return ResponseEntity.ok(DataResponse.of(oAuthLoginService.tokenRegenerate(tokenRegenerateRequest)));
    }

    @Operation(summary = "로그아웃")
    @PostMapping("/logout")
    public ResponseEntity<DataResponse<CustomResponseCode>> logout(@Validated @RequestBody LogoutRequest logoutRequest) {
        oAuthLoginService.logout(logoutRequest);
        return ResponseEntity.ok(DataResponse.of(CustomResponseCode.MEMBER_LOGOUT));
    }

    @Operation(summary = "회원탈퇴")
    @DeleteMapping("/deactivation")
    public ResponseEntity<DataResponse<CustomResponseCode>> deactivation() {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        UUID memberId = UUID.fromString(loggedInUser.getName());
        oAuthLoginService.deactivation(memberId);
        return ResponseEntity.ok(DataResponse.of(CustomResponseCode.MEMBER_DEACTIVATION));
    }

    @GetMapping("/kakao/code")
    public ResponseEntity<String> code(@RequestParam String code) {
        return ResponseEntity.ok(code);
    }
}
