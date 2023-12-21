package com.fubao.project.domain.service;

import com.fubao.project.domain.api.post.dto.request.PostWriteRequest;
import com.fubao.project.domain.api.post.dto.response.PostGetResponse;
import com.fubao.project.domain.api.post.dto.response.PostPatchResponse;
import com.fubao.project.domain.api.post.dto.response.PostWriteResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface PostService {
    PostWriteResponse post(MultipartFile images, PostWriteRequest postWriteRequest, UUID memberId);

    PostPatchResponse patch(MultipartFile images, PostWriteRequest postWriteRequest, UUID memberId, Long postId);

    PostGetResponse get(Long postId);
}
