package com.haem.blogbackend.dto.response;

public record ApiResponse<T>(
        T result,
        String message,
        Integer status
) {
    public static <T> ApiResponse<T> ok(T result) {
        return new ApiResponse<>(result, "success", null);
    }

    public static <T> ApiResponse<T> ok(T result, int status) {
        return new ApiResponse<>(result, "success", status);
    }

    public static <T> ApiResponse<T> error(String message, int status) {
        return new ApiResponse<>(null, message, status);
    }
}
