package com.haem.blogbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CategoryUpdateRequestDto {

    @NotBlank(message = "카테고리명은 필수 입력 값입니다.")
    private String categoryName;

    public CategoryUpdateRequestDto() {}
}
