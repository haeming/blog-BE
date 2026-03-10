package com.haem.blogbackend.comment.application.dto;

public record CommentUpdateCommand(
        String password,
        String content
) {}