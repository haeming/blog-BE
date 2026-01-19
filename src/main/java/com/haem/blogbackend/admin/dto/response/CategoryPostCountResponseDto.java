package com.haem.blogbackend.admin.dto.response;

import com.haem.blogbackend.domain.category.dto.CategoryPostCountView;

public class CategoryPostCountResponseDto {
    private final Long id;
    private final String name;
    private final long postCount;

    public CategoryPostCountResponseDto(Long id, String name, long postCount) {
        this.id = id;
        this.name = name;
        this.postCount = postCount;
    }

    public static CategoryPostCountResponseDto from(CategoryPostCountView view){
        return new CategoryPostCountResponseDto(
                view.getCategoryId(),
                view.getCategoryName(),
                view.getPostCount()
        );
    }
    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public long getPostCount() {
        return postCount;
    }
}
