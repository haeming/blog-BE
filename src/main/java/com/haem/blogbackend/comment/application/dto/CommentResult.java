package com.haem.blogbackend.comment.application.dto;

import com.haem.blogbackend.comment.domain.Comment;

import java.time.LocalDateTime;

public record CommentResult(
        Long id,
        Long postId,
        Long adminId,
        Long parentId,
        String nickname,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static CommentResult from(Comment comment) {
        return new CommentResult(
                comment.getId(),
                comment.getPost().getId(),
                comment.getAdmin() != null ? comment.getAdmin().getId() : null,
                comment.getParent() != null ? comment.getParent().getId() : null,
                comment.getNickname(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }
}