package com.haem.blogbackend.common.exception.token;

import com.haem.blogbackend.common.exception.base.TokenException;

public class ExpiredTokenException extends TokenException{
    public ExpiredTokenException() {
        super("토큰이 만료되었습니다.");
    }

    public ExpiredTokenException(String message) {
        super(message);
    }
}
