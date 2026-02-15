package com.haem.blogbackend.comment.api.dto;

import java.time.LocalDateTime;

import com.haem.blogbackend.comment.application.dto.CommentResult;
import com.haem.blogbackend.comment.domain.Comment;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CommentResponseDto {
    private final Long id;
    private final Long postId;
    private final Long adminId;
    private final Long parentId;
    private final String nickname;
    private final String content;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    private CommentResponseDto(Long id, Long postId, Long adminId, Long parentId, String nickname,
                               String content, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.postId = postId;
        this.adminId = adminId;
        this.parentId = parentId;
        this.nickname = nickname;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static CommentResponseDto from(Comment comment) {
        return CommentResponseDto.builder()
                .id(comment.getId())
                .postId(comment.getPost().getId())
                .adminId(comment.getAdmin() != null ? comment.getAdmin().getId() : null)
                .parentId(comment.getParent() != null ? comment.getParent().getId() : null)
                .nickname(comment.getNickname())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }

    public static CommentResponseDto from(CommentResult result) {
        return CommentResponseDto.builder()
                .id(result.id())
                .postId(result.postId())
                .adminId(result.adminId())
                .parentId(result.parentId())
                .nickname(result.nickname())
                .content(result.content())
                .createdAt(result.createdAt())
                .updatedAt(result.updatedAt())
                .build();
    }
}