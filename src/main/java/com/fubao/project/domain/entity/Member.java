package com.fubao.project.domain.entity;

import com.fubao.project.global.common.constant.MemberRole;
import com.fubao.project.global.common.constant.OAuthProvider;
import com.fubao.project.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.Authentication;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member")
public class Member extends BaseEntity {

    @Id
    @Column(columnDefinition = "BINARY(16)", name = "id")
    private UUID id;

    @Column(name = "oauth_provider")
    @Enumerated(EnumType.STRING)
    private OAuthProvider oauthProvider;

    @Column(name = "provider_id")
    private String providerId;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private MemberRole memberRole;

    public static Member of(
            UUID id, OAuthProvider oAuthProvider, String providerId, MemberRole memberRole
    ) {
        return Member.builder()
                .id(id)
                .memberRole(memberRole)
                .providerId(providerId)
                .oAuthProvider(oAuthProvider)
                .build();
    }

    @Builder
    public Member(UUID id, OAuthProvider oAuthProvider, String providerId, MemberRole memberRole) {
        this.id = id;
        this.oauthProvider = oAuthProvider;
        this.providerId = providerId;
        this.memberRole = memberRole;
    }
}
