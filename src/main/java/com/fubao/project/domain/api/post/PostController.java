package com.fubao.project.domain.api.post;

import com.fubao.project.domain.api.post.dto.request.PostWriteRequest;
import com.fubao.project.domain.api.post.dto.response.PostWriteResponse;
import com.fubao.project.domain.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Validated
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/post")
@Tag(name = "coach", description = "코치 API")
public class PostController {
    private final PostService postService;

    @Operation(summary = "편지쓰기")
    @PostMapping(value = "", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<PostWriteResponse> postWrite(@RequestPart(value = "image") MultipartFile images,
                                                       @RequestPart(value = "data") @Validated PostWriteRequest postWriteRequest
    ) {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        UUID memberId = UUID.fromString(loggedInUser.getName());
        return ResponseEntity.ok(postService.post(images, postWriteRequest, memberId));
    }
}
