package com.haem.blogbackend.dto.response;

import com.haem.blogbackend.domain.Post;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostSummaryResponseDto {
    private long id;
    private String title;
    private String content;
    private String categoryName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public PostSummaryResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.categoryName = post.getCategory()!= null ? post.getCategory().getCategoryName() : null;
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
    }
}
