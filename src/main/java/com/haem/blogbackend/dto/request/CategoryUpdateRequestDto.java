package com.haem.blogbackend.dto.request;

import lombok.Getter;

@Getter
public class CategoryUpdateRequestDto {
    private Long id;
    private String categoryName;

    public CategoryUpdateRequestDto() {}
}
