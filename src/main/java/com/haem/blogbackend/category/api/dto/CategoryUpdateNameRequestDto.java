package com.haem.blogbackend.category.api.dto;

import lombok.Getter;

@Getter
public class CategoryUpdateNameRequestDto {

    private String categoryName;

    public CategoryUpdateNameRequestDto() {}

    public CategoryUpdateNameRequestDto(String categoryName) {
        this.categoryName = categoryName;
    }

}
