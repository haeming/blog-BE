package com.haem.blogbackend.global.error.exception.base;

public abstract class TooManyRequestsException extends RuntimeException {
    public TooManyRequestsException(String message) {
        super(message);
    }
}
