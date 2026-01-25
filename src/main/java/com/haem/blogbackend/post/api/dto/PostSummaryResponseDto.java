package com.haem.blogbackend.post.api.dto;

import java.time.LocalDateTime;

import com.haem.blogbackend.post.application.dto.PostSummaryResult;
import com.haem.blogbackend.post.domain.Post;

import lombok.Getter;

@Getter
public class PostSummaryResponseDto {
    private final Long id;
    private final String title;
    private final String content;
    private final String categoryName;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    private PostSummaryResponseDto(Long id, String title, String content, String categoryName, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.categoryName = categoryName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static PostSummaryResponseDto from(Post post, String categoryName){
        return new PostSummaryResponseDto(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                categoryName,
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }

    public static PostSummaryResponseDto from(PostSummaryResult result){
        return new PostSummaryResponseDto(
                result.id(),
                result.title(),
                result.content(),
                result.categoryName(),
                result.createdAt(),
                result.updatedAt()
        );
    }
}
