package com.haem.blogbackend.comment.api.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PublicCommentUpdateRequestDto {
    private String password;    // 게스트 댓글 수정 시 필요
    private String content;
}
