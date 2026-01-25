package com.haem.blogbackend.category.api.dto;

import com.haem.blogbackend.category.infrastructure.CategoryPostCountView;
import lombok.Builder;

@Builder
public class CategoryPostCountResponseDto {
    private final Long id;
    private final String name;
    private final Long postCount;

    public CategoryPostCountResponseDto(Long id, String name, long postCount) {
        this.id = id;
        this.name = name;
        this.postCount = postCount;
    }

    public static CategoryPostCountResponseDto from(CategoryPostCountView view){
        return CategoryPostCountResponseDto.builder()
                .id(view.getCategoryId())
                .name(view.getCategoryName())
                .postCount(view.getPostCount())
                .build();
    }
    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public Long getPostCount() {
        return postCount;
    }
}
