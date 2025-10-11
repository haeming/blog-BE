package com.haem.blogbackend.exception;

public abstract class TokenException extends RuntimeException {
    public TokenException(String message) {
        super(message);
    }
}
