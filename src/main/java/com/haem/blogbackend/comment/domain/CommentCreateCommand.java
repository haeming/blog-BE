package com.haem.blogbackend.comment.domain;

public interface CommentCreateCommand {
    Long postId();
    Long parentId();
    String content();
}
