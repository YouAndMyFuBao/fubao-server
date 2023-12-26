package com.fubao.project.domain.entity;

import com.fubao.project.global.common.constant.MemberRole;
import com.fubao.project.global.common.constant.OAuthProvider;
import com.fubao.project.global.common.constant.State;
import com.fubao.project.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member")
@Where(clause = "state = 'A'")
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

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private State state;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    List<Post> postList = new ArrayList<>();

    public static Member of(
            UUID id, OAuthProvider oAuthProvider, String providerId, MemberRole memberRole, State state
    ) {
        return Member.builder()
                .id(id)
                .memberRole(memberRole)
                .providerId(providerId)
                .oAuthProvider(oAuthProvider)
                .state(state)
                .build();
    }

    @Builder
    public Member(UUID id, OAuthProvider oAuthProvider, String providerId, MemberRole memberRole, List<Post> postList, State state) {
        this.id = id;
        this.oauthProvider = oAuthProvider;
        this.providerId = providerId;
        this.memberRole = memberRole;
        this.state = state;
        if (postList.isEmpty()) {
            this.postList = postList;
        } else {
            this.postList = null;
        }
    }

    public void deactivation() {
        this.state = State.D;
    }
}
