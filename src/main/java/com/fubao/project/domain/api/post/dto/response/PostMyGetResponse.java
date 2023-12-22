package com.fubao.project.domain.api.post.dto.response;

import com.fubao.project.domain.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostMyGetResponse {
    String imageUrl;
    String content;
    LocalDateTime time;
    public PostMyGetResponse(Post post) {
        this.imageUrl = post.getImageUrl();
        this.content = post.getContent();
        this.time = post.getCreatedAt();
    }
}
