package com.haem.blogbackend.global.error.exception.base;

public abstract class TokenException extends UnauthorizedException {
    public TokenException(String message) {
        super(message);
    }
}
