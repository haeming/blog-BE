package com.haem.blogbackend.admin.dto.request;

import com.haem.blogbackend.domain.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CategoryCreateRequestDto {

    @NotBlank(message = "카테고리 이름은 필수입니다.")
    @Size(max = 50, message = "카테고리 이름은 50자 이하여야 합니다.")
    private String categoryName;

    private String imageUrl;
    private String originalName;

    protected CategoryCreateRequestDto() {}

    private CategoryCreateRequestDto(String categoryName, String imageUrl, String originalName) {
        this.categoryName = categoryName;
        this.imageUrl = imageUrl;
        this.originalName = originalName;
    }

    public static CategoryCreateRequestDto from(Category category){
        return CategoryCreateRequestDto.builder()
                .categoryName(category.getCategoryName())
                .imageUrl(category.getImageUrl())
                .originalName(category.getOriginalName())
                .build();
    }
}
