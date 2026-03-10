package com.haem.blogbackend.global.error.exception.base;

public abstract class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}