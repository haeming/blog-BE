package com.haem.blogbackend.dto.response;

import lombok.Getter;

@Getter
public class CategoryCountResponseDto {
    private final long count;

    public CategoryCountResponseDto(long count) {
        this.count = count;
    }
}
