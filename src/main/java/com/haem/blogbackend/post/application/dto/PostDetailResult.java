package com.haem.blogbackend.post.application.dto;

import com.haem.blogbackend.post.domain.Post;

import java.time.LocalDateTime;
import java.util.List;

public record PostDetailResult(
        Long id,
        String title,
        String content,
        Long categoryId,
        String categoryName,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<ImageItem> images
) {
    public record ImageItem(String url, String originalName) {}

    public static PostDetailResult from(Post post) {
        String categoryName = post.getCategory() != null
                ? post.getCategory().getCategoryName()
                : null;

        Long categoryId = post.getCategory() != null
                ? post.getCategory().getId()
                : null;

        List<ImageItem> images = post.getImages() == null
                ? List.of()
                : post.getImages().stream()
                .map(img -> new ImageItem(
                        img.getImageUrl(),
                        img.getOriginalName()
                ))
                .toList();

        return new PostDetailResult(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                categoryId,
                categoryName,
                post.getCreatedAt(),
                post.getUpdatedAt(),
                images
        );
    }
}
