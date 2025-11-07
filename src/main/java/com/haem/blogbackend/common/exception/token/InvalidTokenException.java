package com.haem.blogbackend.common.exception.token;

import com.haem.blogbackend.common.exception.base.TokenException;

public class InvalidTokenException extends TokenException{
    public InvalidTokenException() {
        super("유효하지 않은 토큰입니다.");
    }

    public InvalidTokenException(String message) {
        super(message);
    }
}
