package com.fubao.project.domain.api.post.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostWriteRequest {
    @NotBlank(message = "게시글 내용은 비워둘 수 없습니다.")
    @Size(max = 150)
    @Schema(description = "게시글 내용", example = "오늘자 한석봉바오 직관")
    private String content;
}
