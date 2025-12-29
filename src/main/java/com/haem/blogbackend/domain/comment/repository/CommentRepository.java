package com.haem.blogbackend.domain.comment.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import com.haem.blogbackend.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostId(Long postId);

    List<Comment> findByPostIdAndDeletedAtIsNull(Long postId);

    @Modifying(flushAutomatically = true)
    @Query("update Comment c set c.deletedAt = CURRENT_TIMESTAMP " +
            "where c.post.id = :postId and c.deletedAt is null")
    int softDeleteByPostId(@Param("postId") Long postId);

    long countByDeletedAtIsNull();
}
