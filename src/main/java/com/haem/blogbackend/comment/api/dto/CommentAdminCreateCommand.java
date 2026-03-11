package com.haem.blogbackend.comment.api.dto;

import com.haem.blogbackend.comment.domain.CommentCreateCommand;

public record CommentAdminCreateCommand(
        Long postId,
        Long parentId,
        String content
) implements CommentCreateCommand {}
