package com.haem.blogbackend.admin.dto.request;

import com.haem.blogbackend.domain.category.entity.Category;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CategoryUpdateImageRequestDto {

    private String imageUrl;
    private String originalName;

    public CategoryUpdateImageRequestDto() {}

    private CategoryUpdateImageRequestDto(String imageUrl, String originalName) {
        this.imageUrl = imageUrl;
        this.originalName = originalName;
    }

    public static CategoryUpdateImageRequestDto from(Category category) {
        return CategoryUpdateImageRequestDto.builder()
                .imageUrl(category.getImageUrl())
                .originalName(category.getOriginalName())
                .build();
    }

}
