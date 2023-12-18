package com.fubao.project.domain.repository;

import com.fubao.project.domain.entity.Member;
import com.fubao.project.global.common.constant.OAuthProvider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MemberRepository extends JpaRepository<Member,UUID> {
    Optional<Member> findByOauthProviderAndProviderId(OAuthProvider oAuthProvider, String snsId);

}
