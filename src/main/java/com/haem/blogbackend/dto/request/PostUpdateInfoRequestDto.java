package com.haem.blogbackend.dto.request;

import lombok.Getter;

@Getter
public class PostUpdateInfoRequestDto {
    private String title;
    private String content;

    protected PostUpdateInfoRequestDto() {}

    public PostUpdateInfoRequestDto(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
