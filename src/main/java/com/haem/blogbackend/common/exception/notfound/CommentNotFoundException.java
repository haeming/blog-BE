package com.haem.blogbackend.common.exception.notfound;

import com.haem.blogbackend.common.exception.base.NotFoundException;

public class CommentNotFoundException extends NotFoundException {
    public CommentNotFoundException(Long id) {
        super(String.format("존재하지 않는 댓글입니다. id = %d", id));
    }
}
