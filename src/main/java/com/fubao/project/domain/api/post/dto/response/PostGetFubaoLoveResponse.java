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
public class PostGetFubaoLoveResponse {
    @Schema(description = "푸바오가 받은 사랑", example = "123456788")
    private Long love;
}
