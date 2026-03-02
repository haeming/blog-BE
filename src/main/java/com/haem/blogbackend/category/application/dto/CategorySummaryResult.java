package com.haem.blogbackend.category.application.dto;

import com.haem.blogbackend.category.domain.Category;

import java.time.LocalDateTime;

public record CategorySummaryResult(
        Long id,
        String categoryName,
        String imageUrl,
        String originalName,
        LocalDateTime createdAt
) {
    public static CategorySummaryResult uncategorized() {
        return new CategorySummaryResult(
                null,
                "미분류",
                null,
                null,
                null
        );
    }

    public static CategorySummaryResult from(Category category) {
        return new CategorySummaryResult(
                category.getId(),
                category.getCategoryName(),
                category.getImageUrl(),
                category.getOriginalName(),
                category.getCreatedAt()
        );
    }
}
