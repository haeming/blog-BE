package com.haem.blogbackend.image.api.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ImageRequestDto {
    private String imageUrl;
    private String originalName;
}
