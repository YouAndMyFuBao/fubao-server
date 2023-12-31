package com.fubao.project.domain.service;

import com.fubao.project.domain.api.post.dto.request.PostWriteRequest;
import com.fubao.project.domain.api.post.dto.response.*;
import com.fubao.project.domain.entity.FubaoLove;
import com.fubao.project.domain.entity.Member;
import com.fubao.project.domain.entity.Post;
import com.fubao.project.domain.repository.FubaoLoveRepository;
import com.fubao.project.domain.repository.MemberRepository;
import com.fubao.project.domain.repository.PostRepository;
import com.fubao.project.global.common.constant.State;
import com.fubao.project.global.common.exception.ResponseCode;
import com.fubao.project.global.common.exception.CustomException;
import com.fubao.project.global.util.DateUtil;
import com.fubao.project.global.util.RedisUtil;
import com.fubao.project.global.util.S3Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostServiceImp implements PostService {
    private final static String DIR = "post";
    private final static String FUBAO_LOVE = "FUBAO_LOVE";
    private final S3Util s3Util;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final FubaoLoveRepository fubaoLoveRepository;
    private final DateUtil dateUtil;
    private final RedisUtil redisUtil;

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
                    .state(State.A)
                    .imageUrl(imageUrl).build();
            save(post);
            addFubaoLove(20L);
        } catch (Exception e) {
            deleteS3Image(imageUrl);
            throw new CustomException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
        return new PostWriteResponse(post.getId());
    }

    @Override
    @Transactional
    public PostPatchResponse patch(MultipartFile image, PostWriteRequest postWriteRequest, UUID memberId, Long postId) {
        Post post = findPostById(postId);
        String imageUrl = null;
        String prevImage = post.getImageUrl();
        if (!post.getMember().getId().equals(memberId))
            throw new CustomException(ResponseCode.DO_NOT_PATCH_POST);
        if (image != null && !image.isEmpty()) {
            imageUrl = uploadS3Image(image);
        }
        try {
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
        return PostGetResponse.builder()
                .date(post.getCreatedAt())
                .imageUrl(post.getImageUrl())
                .content(post.getContent()).build();
    }

    @Override
    public Page<PostMailBoxGetResponse> getMailBox(Pageable pageable) {
        Page<Post> postList = postRepository.findAll(pageable);
        return postList.map(
                post -> PostMailBoxGetResponse.builder()
                        .postId(post.getId())
                        .content(post.getContent())
                        .date(dateUtil.dateFormatToPost(post.getCreatedAt()))
                        .imageUrl(post.getImageUrl()).build()
        );
    }

    @Override
    public List<PostMyGetResponse> myPostGet(UUID memberId) {
        Member member = findMember(memberId);
        List<Post> myPostList = member.getPostList();
        List<PostMyGetResponse> postMyGetResponseList = myPostList.stream().map(
                post -> PostMyGetResponse.builder()
                        .postId(post.getId())
                        .content(post.getContent())
                        .date(dateUtil.dateFormatToPost(post.getCreatedAt()))
                        .imageUrl(post.getImageUrl()).build()
        ).collect(Collectors.toList());
        Collections.reverse(postMyGetResponseList);
        return postMyGetResponseList;
    }

    @Override
    public byte[] getImage(Long postId) {
        Post post = findPostById(postId);
        return s3Util.downloadS3Image(post.getImageUrl());
    }

    @Override
    public void deletePost(Long postId, UUID memberId) {
        Post post = findPostById(postId);
        if (!post.getMember().getId().equals(memberId))
            throw new CustomException(ResponseCode.DO_NOT_DELETE_POST);
        post.delete();
    }

    @Override
    public void addFubaoLove() {
        addFubaoLove(1L);
    }

    private Post findPostById(Long postId) {
        return postRepository.findById(postId).orElseThrow(() -> new CustomException(ResponseCode.POST_NOT_FOUND));
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

    private void addFubaoLove(Long love) {
        FubaoLove fubaoLove = fubaoLoveRepository.getReferenceById(1L);
        if (!redisUtil.hasKey(FUBAO_LOVE)) {
            redisUtil.setStringData(FUBAO_LOVE, String.valueOf(fubaoLove.getLove()), Duration.ofHours(6));
        }
        redisUtil.incrementValue(FUBAO_LOVE, love);
    }

    @Scheduled(fixedDelay = 1000 * 60 * 60)
    @Transactional
    protected void updateLove() {
        FubaoLove fubaoLove = fubaoLoveRepository.getReferenceById(1L);
        if (redisUtil.hasKey(FUBAO_LOVE)) {
            Long love = Long.valueOf(redisUtil.getData(FUBAO_LOVE));
            fubaoLove.updateLove(love);
        }
    }

    private void save(Post post) {
        postRepository.save(post);
    }
}
