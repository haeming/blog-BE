package com.haem.blogbackend.dto.response;

import com.haem.blogbackend.domain.Image;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ImageResponseDto {
    private Long id;
    private String imageUrl;
    private String originalName;

    public static ImageResponseDto from(Image image) {
        return ImageResponseDto.builder()
                .id(image.getId())
                .imageUrl(image.getImageUrl())
                .originalName(image.getOriginalName())
                .build();
    }
}
