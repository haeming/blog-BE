package com.haem.blogbackend.dto.response;

import com.haem.blogbackend.domain.Image;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ImageResponseDto {
    private final Long id;
    private final String imageUrl;
    private final String originalName;

    private ImageResponseDto(Long id, String imageUrl, String originalName) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.originalName = originalName;
    }

    public static ImageResponseDto from(Image image) {
        return ImageResponseDto.builder()
                .id(image.getId())
                .imageUrl(image.getImageUrl())
                .originalName(image.getOriginalName())
                .build();
    }
}
