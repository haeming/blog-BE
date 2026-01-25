package com.haem.blogbackend.comment.domain;

import com.haem.blogbackend.global.error.exception.base.NotFoundException;

public class CommentNotFoundException extends NotFoundException {
    public CommentNotFoundException(Long id) {
        super(String.format("존재하지 않는 댓글입니다. id = %d", id));
    }
}
