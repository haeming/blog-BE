package com.haem.blogbackend.comment.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PublicCommentCreateRequestDto {
    @NotNull(message = "게시글 ID는 필수입니다.")
    @Positive(message = "게시글 ID는 1 이상이어야 합니다.")
    private Long postId;

    @Positive(message = "부모 댓글 ID는 1 이상이어야 합니다.")
    private Long parentId;

    @NotBlank(message = "닉네임은 필수입니다.")
    @Size(max = 100, message = "닉네임은 100자 이하여야 합니다.")
    private String nickname;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(max = 100, message = "비밀번호는 100자 이하여야 합니다.")
    private String password;

    @NotBlank(message = "댓글 내용은 필수입니다.")
    private String content;
}
