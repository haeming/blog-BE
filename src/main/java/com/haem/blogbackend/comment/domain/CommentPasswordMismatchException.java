package com.haem.blogbackend.comment.domain;

import com.haem.blogbackend.global.error.exception.base.BadRequestException;

public class CommentPasswordMismatchException extends BadRequestException {
    public CommentPasswordMismatchException() {
        super("댓글 비밀번호가 일치하지 않습니다.");
    }
}