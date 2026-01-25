package com.haem.blogbackend.global.error.exception.base;

public abstract class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}

