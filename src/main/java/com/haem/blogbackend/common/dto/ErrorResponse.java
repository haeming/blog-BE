package com.haem.blogbackend.common.dto;

import org.springframework.http.HttpStatus;

public record ErrorResponse(
        String message,
        int status,
        String code
) {
    public static ErrorResponse of(String message, HttpStatus status) {
        return new ErrorResponse(message, status.value(), null);
    }
}
