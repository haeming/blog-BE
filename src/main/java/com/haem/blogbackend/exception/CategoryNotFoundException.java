package com.haem.blogbackend.exception;

public class CategoryNotFoundException extends RuntimeException{
    public CategoryNotFoundException(Long id) {
        super("존재하지 않는 카테고리입니다. id=" + id);
    }
}
