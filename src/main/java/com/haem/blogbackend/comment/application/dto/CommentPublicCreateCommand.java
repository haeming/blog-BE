package com.haem.blogbackend.comment.application.dto;

import com.haem.blogbackend.comment.domain.CommentCreateCommand;

public record CommentPublicCreateCommand(
        Long postId,
        Long parentId,
        String nickname,
        String password,
        String content
) implements CommentCreateCommand {}