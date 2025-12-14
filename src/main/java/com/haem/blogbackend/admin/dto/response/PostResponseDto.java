package com.haem.blogbackend.admin.dto.response;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.haem.blogbackend.domain.Post;

import lombok.Getter;

@Getter
public class PostResponseDto {
    private final Long id;
    private final String title;
    private final String content;
    private final Long categoryId;
    private final String categoryName;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    private final List<ImageResponseDto> images;

    private PostResponseDto(Long id, String title, String content, Long categoryId, String categoryName,
                            LocalDateTime createdAt, LocalDateTime updatedAt, List<ImageResponseDto> images) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.images = images;
    }

    public static PostResponseDto from(Post post) {
        List<ImageResponseDto> images = post.getImages() != null
                ? post.getImages().stream().map(ImageResponseDto::from).toList()
                : new ArrayList<>();

        return new PostResponseDto(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getCategory() != null ? post.getCategory().getId() : null,
                post.getCategory() != null ? post.getCategory().getCategoryName() : null,
                post.getCreatedAt(),
                post.getUpdatedAt(),
                images
        );
    }
}
