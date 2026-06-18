package com.haem.blogbackend.post.application.dto;

public record PostAdjacentResult(
        AdjacentPostResult prev,
        AdjacentPostResult next
) {}
