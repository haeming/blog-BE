package com.haem.blogbackend.post.api.dto;

import com.haem.blogbackend.post.application.dto.PostAdjacentResult;
import lombok.Getter;

@Getter
public class PostAdjacentResponseDto {
    private final AdjacentPostResponseDto prev;
    private final AdjacentPostResponseDto next;

    private PostAdjacentResponseDto(AdjacentPostResponseDto prev, AdjacentPostResponseDto next) {
        this.prev = prev;
        this.next = next;
    }

    public static PostAdjacentResponseDto from(PostAdjacentResult result) {
        AdjacentPostResponseDto prev = result.prev() != null
                ? AdjacentPostResponseDto.from(result.prev())
                : null;
        AdjacentPostResponseDto next = result.next() != null
                ? AdjacentPostResponseDto.from(result.next())
                : null;
        return new PostAdjacentResponseDto(prev, next);
    }
}
