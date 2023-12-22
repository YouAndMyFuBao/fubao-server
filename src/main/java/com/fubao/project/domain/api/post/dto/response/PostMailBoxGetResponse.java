package com.fubao.project.domain.api.post.dto.response;

import com.fubao.project.domain.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostMailBoxGetResponse {
    String imageUrl;
    String content;
    LocalDateTime time;
    public PostMailBoxGetResponse(Post post) {
        this.imageUrl = post.getImageUrl();
        this.content = post.getContent();
        this.time = post.getCreatedAt();
    }
}
