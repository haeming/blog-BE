package com.haem.blogbackend.dto.response;

import java.time.LocalDateTime;

import com.haem.blogbackend.domain.Post;

import lombok.Getter;

@Getter
public class PostSummaryResponseDto {
    private Long id;
    private String title;
    private String content;
    private Long categoryId;
    private String categoryName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private PostSummaryResponseDto(Long id, String title, String content, Long categoryId, String categoryName, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static PostSummaryResponseDto from(Post post){
        return new PostSummaryResponseDto(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getCategory().getId(),
                post.getCategory().getCategoryName(),
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }
}
