package com.haem.blogbackend.exception;

public class PostNotFoundException extends NotFoundException{
    public PostNotFoundException(Long id) {
        super("존재하지 않는 포스트입니다. id=" + id);
    }
}
