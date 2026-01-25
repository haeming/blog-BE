package com.haem.blogbackend.auth.domain;

import com.haem.blogbackend.global.error.exception.base.TokenException;

public class InvalidTokenException extends TokenException{
    public InvalidTokenException() {
        super("유효하지 않은 토큰입니다.");
    }

    public InvalidTokenException(String message) {
        super(message);
    }
}
