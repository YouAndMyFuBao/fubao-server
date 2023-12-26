package com.fubao.project.domain.repository;

import com.fubao.project.domain.entity.Post;
import com.fubao.project.global.common.constant.State;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findAllByState(Pageable pageable, State state);
}
