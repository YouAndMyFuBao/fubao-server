package com.fubao.project.domain.api.post.dto.response;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostGetResponse {
    @Schema(description = "게시글 이미지 주소", example = "https://d3hubo81q4ghxt.cloudfront.net/local/post/3cebc0ed-f1dd-4fab-9351-96efa8b912fa.jpg")
    private String imageUrl;
    @Schema(description = "게시글 내용", example = "hiii222")
    private String content;
    @Schema(description = "게시글 생성일", example = "2023-12-20T02:08:50")
    private LocalDateTime date;
}
