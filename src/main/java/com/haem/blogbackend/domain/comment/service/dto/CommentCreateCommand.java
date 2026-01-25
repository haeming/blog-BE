package com.haem.blogbackend.domain.comment.service.dto;

public record CommentCreateCommand(Long postId, Long parentId, String content) {

}
