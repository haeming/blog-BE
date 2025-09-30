package com.haem.blogbackend.dto.response;

import com.haem.blogbackend.domain.Category;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CategoryResponseDto {
    private Long id;
    private String categoryName;
    private LocalDateTime createdAt;

    public CategoryResponseDto(Category category) {
        this.id = category.getId();
        this.categoryName = category.getCategoryName();
        this.createdAt = category.getCreatedAt();
    }
}
