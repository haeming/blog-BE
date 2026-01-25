package com.haem.blogbackend.global.error.exception.base;

public abstract class TokenException extends RuntimeException {
    public TokenException(String message) {
        super(message);
    }
}
