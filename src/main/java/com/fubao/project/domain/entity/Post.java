package com.fubao.project.domain.entity;

import com.fubao.project.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "post")
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

    public static Post of(
            Long id, Member member, String imageUrl, String content
    ) {
        return Post.builder()
                .id(id)
                .member(member)
                .imageUrl(imageUrl)
                .content(content)
                .build();
    }

    @Builder
    public Post(Long id, Member member, String imageUrl, String content) {
        this.id = id;
        this.member = member;
        this.imageUrl = imageUrl;
        this.content = content;
    }
}