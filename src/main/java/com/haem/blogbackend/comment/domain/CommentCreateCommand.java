package com.haem.blogbackend.comment.domain;

public record CommentCreateCommand(Long postId, Long parentId, String content) {

}
