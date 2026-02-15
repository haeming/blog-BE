package com.haem.blogbackend.comment.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AdminCommentUpdateRequestDto {
    @NotBlank(message = "댓글 내용은 필수입니다.")
    private String content;

    protected AdminCommentUpdateRequestDto() {}

    private AdminCommentUpdateRequestDto(String content) {
        this.content = content;
    }
}