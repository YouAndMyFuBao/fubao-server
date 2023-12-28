package com.fubao.project.domain.service;

import com.fubao.project.domain.api.post.dto.request.PostWriteRequest;
import com.fubao.project.domain.api.post.dto.response.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface PostService {
    PostWriteResponse post(MultipartFile images, PostWriteRequest postWriteRequest, UUID memberId);

    PostPatchResponse patch(MultipartFile images, PostWriteRequest postWriteRequest, UUID memberId, Long postId);

    PostGetResponse get(Long postId);

    Page<PostMailBoxGetResponse> getMailBox(Pageable pageable);

    List<PostMyGetResponse> myPostGet(UUID memberId);

    byte[] getImage(Long postId);

    void deletePost(Long postId, UUID memberId);
}
