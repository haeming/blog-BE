package com.haem.blogbackend.comment.api.dto;

import com.haem.blogbackend.comment.domain.CommentUpdateCommand;

public record CommentAdminUpdateCommand(
        String content
) implements CommentUpdateCommand {}