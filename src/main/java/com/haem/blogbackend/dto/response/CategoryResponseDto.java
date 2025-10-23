package com.haem.blogbackend.dto.response;

import java.time.LocalDateTime;

import com.haem.blogbackend.domain.Category;

import lombok.Getter;

@Getter
public class CategoryResponseDto {
    private final Long id;
    private final String categoryName;
    private final String imageUrl;
    private final String originalName;
    private final LocalDateTime createdAt;

    private CategoryResponseDto(Long id, String categoryName, String imageUrl, String originalName, LocalDateTime createdAt) {
        this.id = id;
        this.categoryName = categoryName;
        this.imageUrl = imageUrl;
        this.originalName = originalName;
        this.createdAt = createdAt;
    }

    public static CategoryResponseDto from(Category category){
        return new CategoryResponseDto(
            category.getId(),
            category.getCategoryName(),
            category.getImageUrl(),
            category.getOriginalName(),
            category.getCreatedAt()
        );
    }
}
