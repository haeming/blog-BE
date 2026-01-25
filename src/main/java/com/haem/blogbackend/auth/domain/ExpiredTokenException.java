package com.haem.blogbackend.auth.domain;

import com.haem.blogbackend.global.error.exception.base.TokenException;

public class ExpiredTokenException extends TokenException {
    public ExpiredTokenException() {
        super("토큰이 만료되었습니다.");
    }

    public ExpiredTokenException(String message) {
        super(message);
    }
}
