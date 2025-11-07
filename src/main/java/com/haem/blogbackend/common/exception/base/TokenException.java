package com.haem.blogbackend.common.exception.base;

public abstract class TokenException extends RuntimeException {
    public TokenException(String message) {
        super(message);
    }
}
