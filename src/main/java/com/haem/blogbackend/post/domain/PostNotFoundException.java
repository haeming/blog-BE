package com.haem.blogbackend.post.domain;

import com.haem.blogbackend.global.error.exception.base.NotFoundException;

public class PostNotFoundException extends NotFoundException {
    public PostNotFoundException(Long id) {
        super(String.format("존재하지 않는 포스트입니다. id = %d", id));
    }
}
