package com.haem.blogbackend.admin.repository;

import com.haem.blogbackend.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
