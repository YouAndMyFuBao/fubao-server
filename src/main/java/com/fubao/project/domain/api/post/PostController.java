package com.fubao.project.domain.api.post;

import com.fubao.project.domain.api.post.dto.request.PostWriteRequest;
import com.fubao.project.domain.api.post.dto.response.*;
import com.fubao.project.domain.service.PostService;
import com.fubao.project.global.common.api.CustomResponseCode;
import com.fubao.project.global.common.exception.ResponseCode;
import com.fubao.project.global.common.exception.CustomException;
import com.fubao.project.global.common.response.DataResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Validated
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/posts")
@Tag(name = "post", description = "편지 API")
public class PostController {
    private final PostService postService;

    @Operation(summary = "편지쓰기")
    @PostMapping(value = "", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<DataResponse<PostWriteResponse>> postWrite(@RequestPart(value = "image") MultipartFile image,
                                                                     @RequestPart(value = "data") @Validated PostWriteRequest postWriteRequest
    ) {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        UUID memberId = UUID.fromString(loggedInUser.getName());
        String contentType = image.getContentType();
        if (contentType != null && !contentType.startsWith("image")) {
            throw new CustomException(ResponseCode.INVALID_FILE);
        }
        return ResponseEntity.ok(DataResponse.of(postService.post(image, postWriteRequest, memberId)));
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
        if (image != null) {
            String contentType = image.getContentType();
            if (contentType != null && !contentType.startsWith("image")) {
                throw new CustomException(ResponseCode.INVALID_FILE);
            }
        }
        return ResponseEntity.ok(DataResponse.of(postService.patch(image, postWriteRequest, memberId, postId)));
    }

    @Operation(summary = "편지 보기")
    @GetMapping(value = "/{postId}")
    public ResponseEntity<DataResponse<PostGetResponse>> postGet(@PathVariable Long postId) {
        return ResponseEntity.ok(DataResponse.of(postService.get(postId)));
    }

    @Operation(summary = "우체통")
    @GetMapping(value = "")
    public ResponseEntity<DataResponse<Page<PostMailBoxGetResponse>>> postMailboxGet(Pageable pageable) {
        return ResponseEntity.ok(DataResponse.of(postService.getMailBox(pageable)));
    }

    @Operation(summary = "내가 쓴편지 보기")
    @GetMapping(value = "/my")
    public ResponseEntity<DataResponse<List<PostMyGetResponse>>> myPostGet() {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        UUID memberId = UUID.fromString(loggedInUser.getName());
        return ResponseEntity.ok(DataResponse.of(postService.myPostGet(memberId)));
    }

    @Operation(summary = "편지 이미지 다운로드")
    @GetMapping(value = "/{postId}/download")
    public ResponseEntity<ByteArrayResource> getImage(@PathVariable Long postId) {
        ByteArrayResource image = postService.getImage(postId);
        return ResponseEntity.ok(image);
    }

    @Operation(summary = "편지 삭제")
    @DeleteMapping(value = "/{postId}")
    public ResponseEntity<DataResponse<CustomResponseCode>> deletePost(@PathVariable Long postId) {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        UUID memberId = UUID.fromString(loggedInUser.getName());
        postService.deletePost(postId, memberId);
        return ResponseEntity.ok(DataResponse.of(CustomResponseCode.POST_DELETE));
    }

    @Operation(summary = "푸바오에게 사랑보내기")
    @PostMapping(value = "/fubao/love")
    public ResponseEntity<DataResponse<CustomResponseCode>> postFubaoLove() {
        postService.addFubaoLove();
        return ResponseEntity.ok(DataResponse.of(CustomResponseCode.FUBAO_LOVE_ADD));
    }

    @Operation(summary = "푸바오받은 사랑")
    @GetMapping(value = "/fubao/love")
    public ResponseEntity<DataResponse<PostGetFubaoLoveResponse>> getFubaoLove() {
        return ResponseEntity.ok(DataResponse.of(postService.getFubaoLove()));
    }
}