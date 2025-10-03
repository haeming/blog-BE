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

    private CategoryResponseDto(Category category) {
        this.id = category.getId();
        this.categoryName = category.getCategoryName();
        this.imageUrl = category.getImageUrl();
        this.originalName = category.getOriginalName();
        this.createdAt = category.getCreatedAt();
    }

    public static CategoryResponseDto from(Category category){
        return new CategoryResponseDto(category);
    }
}
