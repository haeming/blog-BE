package com.haem.blogbackend.comment.domain;

import com.haem.blogbackend.global.error.exception.base.BadRequestException;

public class CommentProfanityException extends BadRequestException {
    public CommentProfanityException() {
        super("댓글에 금칙어가 포함되어 있습니다.");
    }
}
