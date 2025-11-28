package com.haem.blogbackend.admin.repository;

import com.haem.blogbackend.dto.response.CommentResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;

import com.haem.blogbackend.domain.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostId(Long postId);
}
