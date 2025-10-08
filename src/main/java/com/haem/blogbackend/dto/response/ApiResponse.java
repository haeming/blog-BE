package com.haem.blogbackend.dto.response;

public record ApiResponse<T>(
        T data,
        String message,
        Integer status
) {
    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(data, "success", null);
    }

    public static <T> ApiResponse<T> ok(T data, int status) {
        return new ApiResponse<>(data, "success", status);
    }

    public static <T> ApiResponse<T> error(String message, int status) {
        return new ApiResponse<>(null, message, status);
    }
}
