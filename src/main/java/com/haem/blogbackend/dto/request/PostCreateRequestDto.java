package com.haem.blogbackend.dto.request;

import com.haem.blogbackend.domain.Post;
import lombok.Getter;

@Getter
public class PostCreateRequestDto {
    private String title;
    private String content;
    private Long categoryId;

    public PostCreateRequestDto(){}
}
