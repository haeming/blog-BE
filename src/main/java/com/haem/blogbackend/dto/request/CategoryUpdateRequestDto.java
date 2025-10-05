package com.haem.blogbackend.dto.request;

import com.haem.blogbackend.domain.Category;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CategoryUpdateRequestDto {

    private String categoryName;
    private String imageUrl;
    private String originalName;

    public CategoryUpdateRequestDto() {}

    public static CategoryUpdateRequestDto from(Category category) {
        return CategoryUpdateRequestDto.builder()
                .categoryName(category.getCategoryName())
                .imageUrl(category.getImageUrl())
                .originalName(category.getOriginalName())
                .build();
    }
}
