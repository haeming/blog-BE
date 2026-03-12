package com.haem.blogbackend.comment.domain;

import com.haem.blogbackend.global.error.exception.base.BadRequestException;

public class CommentRateLimitExceededException extends BadRequestException {
    public CommentRateLimitExceededException() {
        super("동일 IP에서 1분에 최대 3개의 댓글만 작성할 수 있습니다.");
    }
}
