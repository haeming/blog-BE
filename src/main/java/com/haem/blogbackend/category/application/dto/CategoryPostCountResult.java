package com.haem.blogbackend.category.application.dto;

import com.haem.blogbackend.category.infrastructure.CategoryPostCountView;

public record CategoryPostCountResult(
        Long id,
        String name,
        long postCount
) {
    public static CategoryPostCountResult from(CategoryPostCountView view) {
        return new CategoryPostCountResult(
                view.getCategoryId(),
                view.getCategoryName(),
                view.getPostCount()
        );
    }
}
