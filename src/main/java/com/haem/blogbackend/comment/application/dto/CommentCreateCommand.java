package com.haem.blogbackend.comment.application.dto;

public record CommentCreateCommand(
        Long postId,
        Long parentId,
        String nickname,
        String password,
        String content
) {}
