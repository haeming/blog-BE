package com.haem.blogbackend.post.api.dto;

import com.haem.blogbackend.post.application.dto.AdjacentPostResult;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AdjacentPostResponseDto {
    private final Long id;
    private final String title;
    private final LocalDateTime createdAt;

    private AdjacentPostResponseDto(Long id, String title, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.createdAt = createdAt;
    }

    public static AdjacentPostResponseDto from(AdjacentPostResult result) {
        return new AdjacentPostResponseDto(
                result.id(),
                result.title(),
                result.createdAt()
        );
    }
}
