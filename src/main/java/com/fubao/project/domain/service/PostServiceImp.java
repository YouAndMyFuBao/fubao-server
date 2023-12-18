package com.fubao.project.domain.service;

import com.fubao.project.domain.api.post.dto.request.PostWriteRequest;
import com.fubao.project.domain.api.post.dto.response.PostWriteResponse;
import com.fubao.project.domain.entity.Member;
import com.fubao.project.domain.entity.Post;
import com.fubao.project.domain.repository.MemberRepository;
import com.fubao.project.domain.repository.PostRepository;
import com.fubao.project.global.common.exception.CustomErrorCode;
import com.fubao.project.global.common.exception.CustomException;
import com.fubao.project.global.util.S3Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public PostWriteResponse post(MultipartFile images, PostWriteRequest postWriteRequest, UUID memberId) {
        String imageUrl = uploadS3Image(images);

        Member member = findMember(memberId);
        Post post = Post.builder()
                .content(postWriteRequest.getContent())
                .member(member)
                .imageUrl(imageUrl).build();

        try {
            save(post);
        } catch (Exception e) {
            deleteS3Image(imageUrl);
            throw new CustomException(CustomErrorCode.INTERNAL_SERVER_ERROR);
        }
        return new PostWriteResponse(post.getId());
    }

    protected String uploadS3Image(MultipartFile images) {
        return s3Util.uploadFileToS3(DIR, images);
    }

    protected void deleteS3Image(String imageUrl) {
        s3Util.delete(imageUrl);
    }

    protected Member findMember(UUID memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new CustomException(CustomErrorCode.USER_NOT_FOUND));
    }

    protected void save(Post post) {
        postRepository.save(post);
    }
}
