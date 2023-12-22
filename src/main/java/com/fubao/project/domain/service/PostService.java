package com.fubao.project.domain.service;

import com.fubao.project.domain.api.post.dto.request.PostWriteRequest;
import com.fubao.project.domain.api.post.dto.response.PostGetResponse;
import com.fubao.project.domain.api.post.dto.response.PostMailBoxGetResponse;
import com.fubao.project.domain.api.post.dto.response.PostPatchResponse;
import com.fubao.project.domain.api.post.dto.response.PostWriteResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface PostService {
    PostWriteResponse post(MultipartFile images, PostWriteRequest postWriteRequest, UUID memberId);

    PostPatchResponse patch(MultipartFile images, PostWriteRequest postWriteRequest, UUID memberId, Long postId);

    PostGetResponse get(Long postId);

    List<PostMailBoxGetResponse> getMailBox(Pageable pageable);
}
