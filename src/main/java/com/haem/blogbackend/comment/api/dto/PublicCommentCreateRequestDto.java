package com.haem.blogbackend.comment.api.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PublicCommentCreateRequestDto {
    private Long postId;
    private Long parentId;      // nullable (대댓글이 아니면 null)
    private Long adminId;       // nullable (게스트면 null)
    private String nickname;    // nullable (어드민이면 null)
    private String password;    // nullable (어드민이면 null)
    private String content;
}
