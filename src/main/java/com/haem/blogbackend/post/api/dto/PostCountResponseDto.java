package com.haem.blogbackend.post.api.dto;

import lombok.Getter;

@Getter
public class PostCountResponseDto {
    private final long count;

    public PostCountResponseDto(long count){
        this.count = count;
    }
}
