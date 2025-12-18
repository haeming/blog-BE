package com.haem.blogbackend.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.haem.blogbackend.domain.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostId(Long postId);

    List<Comment> findByPostIdAndDeletedAtIsNull(Long postId);

    long countByDeletedAtIsNull();
}
