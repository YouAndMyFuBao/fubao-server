package com.fubao.project.domain.service;

import com.fubao.project.domain.api.auth.dto.request.LogoutRequest;
import com.fubao.project.domain.api.auth.dto.request.TokenRegenerateRequest;
import com.fubao.project.domain.api.auth.dto.response.AuthTokens;
import com.fubao.project.domain.entity.Member;
import com.fubao.project.domain.entity.Post;
import com.fubao.project.domain.repository.MemberRepository;
import com.fubao.project.domain.repository.PostRepository;
import com.fubao.project.domain.service.oauth.RequestOAuthInfoService;
import com.fubao.project.global.common.constant.MemberRole;
import com.fubao.project.global.common.constant.State;
import com.fubao.project.global.common.exception.ResponseCode;
import com.fubao.project.global.common.exception.CustomException;
import com.fubao.project.global.common.oauth.OAuthInfoResponse;
import com.fubao.project.global.common.oauth.OAuthLoginParams;
import com.fubao.project.global.config.security.jwt.JwtTokenProvider;
import com.fubao.project.global.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OAuthLoginServiceImp implements OAuthLoginService {

    private final RequestOAuthInfoService requestOAuthInfoService;
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisUtil redisUtil;

    @Override
    @Transactional
    public AuthTokens login(OAuthLoginParams params) {
        OAuthInfoResponse oAuthInfoResponse = requestOAuthInfoService.request(params);
        Member member = findOrCreateMember(oAuthInfoResponse);
        return jwtTokenProvider.createAccessToken(member.getId().toString());
    }

    @Override
    public AuthTokens tokenRegenerate(TokenRegenerateRequest tokenRegenerateRequest) {
        jwtTokenProvider.validateToken(tokenRegenerateRequest.getRefreshToken(),null);
        if (!redisUtil.hasKey(tokenRegenerateRequest.getRefreshToken())) {
            throw new CustomException(ResponseCode.INVALID_TOKEN);
        }
        redisUtil.deleteData(tokenRegenerateRequest.getRefreshToken());
        String userName = jwtTokenProvider.getUsernameFromRefreshToken(tokenRegenerateRequest.getRefreshToken());
        return jwtTokenProvider.createAccessToken(userName);
    }

    @Override
    public void logout(LogoutRequest logoutRequest) {
        redisUtil.deleteData(logoutRequest.getRefreshToken());
    }

    @Override
    @Transactional
    public void deactivation(UUID memberId) {
        Member member = findById(memberId);
        List<Post> postList = member.getPostList();
        postList.forEach(
                Post::delete
        );
        member.deactivation();
        requestOAuthInfoService.disconnect(member.getOauthProvider(),member.getProviderId());
    }

    private Member findById(UUID memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new CustomException(ResponseCode.USER_NOT_FOUND));
    }


    private Member findOrCreateMember(OAuthInfoResponse oAuthInfoResponse) {
        return memberRepository.findByOauthProviderAndProviderId(oAuthInfoResponse.getOAuthProvider(), oAuthInfoResponse.getProviderId())
                .orElseGet(() -> signUp(oAuthInfoResponse));
    }

    private Member signUp(OAuthInfoResponse oAuthInfoResponse) {
        Member member = Member.builder()
                .id(UUID.randomUUID())
                .oAuthProvider(oAuthInfoResponse.getOAuthProvider())
                .providerId(oAuthInfoResponse.getProviderId())
                .memberRole(MemberRole.MEMBER)
                .state(State.A)
                .build();
        return memberRepository.save(member);
    }
}
