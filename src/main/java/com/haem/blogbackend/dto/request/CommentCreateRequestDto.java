package com.haem.blogbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CommentCreateRequestDto {
    @NotNull
    private Long postId;

    private Long adminId;
    private Long parentId;

    @NotBlank(message = "닉네임은 필수입니다.")
    private String nickname;

    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;

    @NotBlank(message = "댓글 내용은 필수입니다.")
    private String content;

    protected CommentCreateRequestDto() {}

    private CommentCreateRequestDto(Long postId, Long adminId, Long parentId, String nickname, String password, String content) {
        this.postId = postId;
        this.adminId = adminId;
        this.parentId = parentId;
        this.nickname = nickname;
        this.password = password;
        this.content = content;
    }

    public static CommentCreateRequestDto from(Long postId, Long adminId, Long parentId, String nickname, String password, String content){
        return CommentCreateRequestDto.builder()
                .postId(postId)
                .adminId(adminId)
                .parentId(parentId)
                .nickname(nickname)
                .password(password)
                .content(content)
                .build();
    }
}
