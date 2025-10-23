package com.haem.blogbackend.dto.response;

import lombok.Getter;

@Getter
public class PostCountResponseDto {
    private final long count;

    public PostCountResponseDto(long count){
        this.count = count;
    }
}
