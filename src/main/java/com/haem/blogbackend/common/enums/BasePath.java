package com.haem.blogbackend.common.enums;

import lombok.Getter;

@Getter
public enum BasePath {
    CATEGORY("blog/category"),
    POST("blog/post");

    private final String path;

    BasePath(String path) {
        this.path = path;
    }
}
