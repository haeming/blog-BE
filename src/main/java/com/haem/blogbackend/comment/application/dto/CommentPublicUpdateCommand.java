package com.haem.blogbackend.comment.application.dto;

import com.haem.blogbackend.comment.domain.CommentUpdateCommand;

public record CommentPublicUpdateCommand(
        String password,
        String content
) implements CommentUpdateCommand {}