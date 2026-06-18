package com.haem.blogbackend.post.application.dto;

import com.haem.blogbackend.post.domain.Post;

import java.time.LocalDateTime;

public record AdjacentPostResult(
        Long id,
        String title,
        LocalDateTime createdAt
) {
    public static AdjacentPostResult from(Post post) {
        return new AdjacentPostResult(
                post.getId(),
                post.getTitle(),
                post.getCreatedAt()
        );
    }
}
