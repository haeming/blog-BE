package com.haem.blogbackend.post.application.dto;

import com.haem.blogbackend.post.domain.Post;

import java.time.LocalDateTime;

public record PostSummaryResult(
        Long id,
        String title,
        String content,
        String categoryName,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static PostSummaryResult from(Post post, String categoryName) {
        return new PostSummaryResult(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                categoryName,
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }
}
