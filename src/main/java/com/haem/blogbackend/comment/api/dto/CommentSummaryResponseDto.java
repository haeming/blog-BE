package com.haem.blogbackend.comment.api.dto;

import com.haem.blogbackend.comment.application.dto.CommentSummaryResult;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentSummaryResponseDto {
    private final Long id;
    private final Long postId;
    private final Long adminId;
    private final Long parentId;
    private final String nickname;
    private final String content;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    private CommentSummaryResponseDto(
            Long id,
            Long postId,
            Long adminId,
            Long parentId,
            String nickname,
            String content,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.postId = postId;
        this.adminId = adminId;
        this.parentId = parentId;
        this.nickname = nickname;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static CommentSummaryResponseDto from(CommentSummaryResult result) {
        return new CommentSummaryResponseDto(
                result.id(),
                result.postId(),
                result.adminId(),
                result.parentId(),
                result.nickname(),
                result.content(),
                result.createdAt(),
                result.updatedAt()
        );
    }
}
