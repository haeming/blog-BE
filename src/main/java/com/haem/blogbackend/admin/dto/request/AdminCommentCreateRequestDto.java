package com.haem.blogbackend.admin.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AdminCommentCreateRequestDto {
    @NotNull
    private Long postId;
    private Long parentId;

    @NotBlank(message = "댓글 내용은 필수입니다.")
    private String content;

    protected AdminCommentCreateRequestDto() {}

    private AdminCommentCreateRequestDto(Long postId, Long parentId, String content) {
        this.postId = postId;
        this.parentId = parentId;
        this.content = content;
    }

    public static AdminCommentCreateRequestDto from(Long postId, Long parentId, String content){
        return AdminCommentCreateRequestDto.builder()
                .postId(postId)
                .parentId(parentId)
                .content(content)
                .build();
    }
}
