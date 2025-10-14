package com.haem.blogbackend.exception.token;

import com.haem.blogbackend.exception.base.TokenException;

public class InvalidTokenException extends TokenException{
    public InvalidTokenException() {
        super("유효하지 않은 토큰입니다.");
    }

    public InvalidTokenException(String message) {
        super(message);
    }
}
