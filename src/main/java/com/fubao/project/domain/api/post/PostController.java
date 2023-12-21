package com.fubao.project.domain.api.post;

import com.fubao.project.domain.api.post.dto.request.PostWriteRequest;
import com.fubao.project.domain.api.post.dto.response.PostPatchResponse;
import com.fubao.project.domain.api.post.dto.response.PostWriteResponse;
import com.fubao.project.domain.service.PostService;
import com.fubao.project.global.common.exception.ResponseCode;
import com.fubao.project.global.common.exception.CustomException;
import com.fubao.project.global.common.response.DataResponse;
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
    public ResponseEntity<DataResponse<PostWriteResponse>> postWrite(@RequestPart(value = "image") MultipartFile images,
                                                                    @RequestPart(value = "data") @Validated PostWriteRequest postWriteRequest
    ) {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        UUID memberId = UUID.fromString(loggedInUser.getName());
        return ResponseEntity.ok(DataResponse.of(postService.post(images, postWriteRequest, memberId)));
    }

    @Operation(summary = "편지수정")
    @PatchMapping(value = "/{postId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<DataResponse<PostPatchResponse>> postPatch(@RequestPart(value = "image", required = false) MultipartFile image,
                                                       @RequestPart(value = "data", required = false) @Validated PostWriteRequest postWriteRequest,
                                                       @PathVariable Long postId) {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        UUID memberId = UUID.fromString(loggedInUser.getName());
        if ((image == null || image.isEmpty()) && postWriteRequest == null)
            throw new CustomException(ResponseCode.PATCH_POST_CONTENT_NOT_EXIST);
        return ResponseEntity.ok(DataResponse.of(postService.patch(image, postWriteRequest, memberId, postId)));
    }
}