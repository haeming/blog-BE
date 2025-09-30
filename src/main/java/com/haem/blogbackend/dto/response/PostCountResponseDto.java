package com.haem.blogbackend.dto.response;

import lombok.Getter;

@Getter
public class PostCountResponseDto {
    private long count;

    public PostCountResponseDto(long count){
        this.count = count;
    }

    public long getCount(){
        return count;
    }
}
