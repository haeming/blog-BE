package com.haem.blogbackend.admin.service.dto;

public record CommentCreateCommand(Long postId, Long parentId, String content) {

}
