package com.haem.blogbackend.dto.request;

import lombok.Getter;

@Getter
public class PostUpdateInfoRequestDto {
    private String title;
    private String content;
    private Long categoryId;

    protected PostUpdateInfoRequestDto() {}

    public PostUpdateInfoRequestDto(String title, String content, Long categoryId) {
        this.title = title;
        this.content = content;
        this.categoryId = categoryId;
    }
}
