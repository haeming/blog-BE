package com.haem.blogbackend.dto.response;

import com.haem.blogbackend.domain.Post;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class PostResponseDto {
    private Long id;
    private String title;
    private String content;
    private Long categoryId;
    private String categoryName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<ImageResponseDto> images;

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.categoryId = post.getCategory() != null ? post.getCategory().getId() : null;
        this.categoryName = post.getCategory() != null ? post.getCategory().getCategoryName() : null;
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();

        this.images = post.getImages() != null ?
                post.getImages().stream()
                        .map(ImageResponseDto::from)
                        .toList()
                :
                new ArrayList<>();
    }
}
