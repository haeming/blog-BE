package com.haem.blogbackend.dto.response;

import com.haem.blogbackend.domain.Category;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CategoryResponseDto {
    private Long id;
    private String categoryName;
    private String imageUrl;
    private String originalName;
    private LocalDateTime createdAt;

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
