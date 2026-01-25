package com.haem.blogbackend.admin.dto.request;

import lombok.Getter;

@Getter
public class CategoryUpdateNameRequestDto {

    private String categoryName;

    public CategoryUpdateNameRequestDto() {}

    public CategoryUpdateNameRequestDto(String categoryName) {
        this.categoryName = categoryName;
    }

}
