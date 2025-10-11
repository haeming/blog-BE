package com.haem.blogbackend.exception;

public class ExpiredTokenException extends TokenException{
    public ExpiredTokenException() {
        super("토큰이 만료되었습니다.");
    }

    public ExpiredTokenException(String message) {
        super(message);
    }
}
