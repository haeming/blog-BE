package com.haem.blogbackend.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.haem.blogbackend.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
