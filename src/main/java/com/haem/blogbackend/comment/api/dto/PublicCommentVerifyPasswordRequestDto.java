package com.haem.blogbackend.comment.api.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PublicCommentVerifyPasswordRequestDto {
    private String password;
}