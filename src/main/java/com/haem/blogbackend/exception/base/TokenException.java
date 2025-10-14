package com.haem.blogbackend.exception.base;

public abstract class TokenException extends RuntimeException {
    public TokenException(String message) {
        super(message);
    }
}
