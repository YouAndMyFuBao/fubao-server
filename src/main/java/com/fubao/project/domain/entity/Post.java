package com.fubao.project.domain.entity;

import com.fubao.project.global.common.entity.BaseEntity;
import com.fubao.project.global.common.constant.State;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "post")
@Where(clause = "state = 'A'")
public class Post extends BaseEntity {
    @Id()
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "content")
    private String content;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private State state;

    public static Post of(
            Long id, Member member, String imageUrl, String content, State state
    ) {
        return Post.builder()
                .id(id)
                .member(member)
                .imageUrl(imageUrl)
                .content(content)
                .state(state)
                .build();
    }

    @Builder
    public Post(Long id, Member member, String imageUrl, String content, State state) {
        this.id = id;
        this.state = state;
        this.member = member;
        this.imageUrl = imageUrl;
        this.content = content;
    }

    public void updateImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void deactivation() {
        this.state = State.D;
    }
}