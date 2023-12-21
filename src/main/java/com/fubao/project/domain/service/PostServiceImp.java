package com.fubao.project.domain.service;

import com.fubao.project.domain.api.post.dto.request.PostWriteRequest;
import com.fubao.project.domain.api.post.dto.response.PostGetResponse;
import com.fubao.project.domain.api.post.dto.response.PostPatchResponse;
import com.fubao.project.domain.api.post.dto.response.PostWriteResponse;
import com.fubao.project.domain.entity.Member;
import com.fubao.project.domain.entity.Post;
import com.fubao.project.domain.repository.MemberRepository;
import com.fubao.project.domain.repository.PostCustomRepository;
import com.fubao.project.domain.repository.PostRepository;
import com.fubao.project.global.common.exception.ResponseCode;
import com.fubao.project.global.common.exception.CustomException;
import com.fubao.project.global.util.S3Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostServiceImp implements PostService {
    private final static String DIR = "post";
    private final S3Util s3Util;
    private final PostRepository postRepository;
    private final PostCustomRepository postCustomRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public PostWriteResponse post(MultipartFile images, PostWriteRequest postWriteRequest, UUID memberId) {
        String imageUrl = uploadS3Image(images);
        Post post;
        try {
            Member member = findMember(memberId);
            post = Post.builder()
                    .content(postWriteRequest.getContent())
                    .member(member)
                    .imageUrl(imageUrl).build();

            save(post);
        } catch (Exception e) {
            deleteS3Image(imageUrl);
            throw new CustomException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
        return new PostWriteResponse(post.getId());
    }

    @Override
    @Transactional
    public PostPatchResponse patch(MultipartFile image, PostWriteRequest postWriteRequest, UUID memberId, Long postId) {
        String imageUrl = null;
        String prevImage = null;
        if (image != null && !image.isEmpty()) {
            imageUrl = uploadS3Image(image);
        }
        Post post;
        try {
            post = findPostById(postId);
            prevImage = post.getImageUrl();
            if (StringUtils.hasText(imageUrl))
                post.updateImageUrl(imageUrl);
            if (postWriteRequest != null)
                post.updateContent(postWriteRequest.getContent());
        } catch (CustomException e) {
            if (imageUrl != null)
                deleteS3Image(imageUrl);
        } catch (Exception e) {
            log.error(e.getMessage());
            if (imageUrl != null)
                deleteS3Image(imageUrl);
        }
        if (StringUtils.hasText(imageUrl) && StringUtils.hasText(prevImage)) {
            deleteS3Image(prevImage);
        }

        return new PostPatchResponse(postId);
    }

    @Override
    public PostGetResponse get(Long postId) {
        Post post = findPostById(postId);
        return new PostGetResponse().builder()
                .date(post.getCreatedAt())
                .imageUrl(post.getImageUrl())
                .content(post.getContent()).build();
    }

    private Post findPostById(Long postId) {
        return postRepository.findById(postId).orElseThrow(() -> new CustomException(ResponseCode.POST_NOT_FOUND));
    }

    private boolean existPost(Long postId) {
        return postRepository.existsById(postId);
    }

    private String uploadS3Image(MultipartFile images) {
        return s3Util.uploadFileToS3(DIR, images);
    }

    private void deleteS3Image(String imageUrl) {
        s3Util.delete(imageUrl);
    }

    private Member findMember(UUID memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new CustomException(ResponseCode.USER_NOT_FOUND));
    }

    private void save(Post post) {
        postRepository.save(post);
    }
}
