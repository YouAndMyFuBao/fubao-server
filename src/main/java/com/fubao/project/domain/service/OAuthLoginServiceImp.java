package com.fubao.project.domain.service;

import com.fubao.project.domain.api.auth.dto.response.AuthTokens;
import com.fubao.project.domain.entity.Member;
import com.fubao.project.domain.repository.MemberRepository;
import com.fubao.project.domain.service.oauth.RequestOAuthInfoService;
import com.fubao.project.global.common.constant.MemberRole;
import com.fubao.project.global.common.oauth.OAuthInfoResponse;
import com.fubao.project.global.common.oauth.OAuthLoginParams;
import com.fubao.project.global.config.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OAuthLoginServiceImp implements OAuthLoginService {

    private final RequestOAuthInfoService requestOAuthInfoService;
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public AuthTokens login(OAuthLoginParams params) {
        OAuthInfoResponse oAuthInfoResponse = requestOAuthInfoService.request(params);
        Member member = findOrCreateMember(oAuthInfoResponse);
        return jwtTokenProvider.createAccessToken(member);
    }


    private Member findOrCreateMember(OAuthInfoResponse oAuthInfoResponse) {
        return memberRepository.findByOauthProviderAndProviderId(oAuthInfoResponse.getOAuthProvider(), oAuthInfoResponse.getProviderId())
                .orElseGet(() -> signUp(oAuthInfoResponse));
    }

    @Transactional
    protected Member signUp(OAuthInfoResponse oAuthInfoResponse) {
        Member member = Member.builder()
                .id(UUID.randomUUID())
                .oAuthProvider(oAuthInfoResponse.getOAuthProvider())
                .providerId(oAuthInfoResponse.getProviderId())
                .memberRole(MemberRole.MEMBER)
                .build();
        return memberRepository.save(member);
    }
}