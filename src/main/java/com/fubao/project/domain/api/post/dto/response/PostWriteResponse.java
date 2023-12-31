package com.fubao.project.domain.api.post.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostWriteResponse {
    @Schema(description = "게시글 Id", example = "작성한 게시글 id")
    private Long postId;
}
