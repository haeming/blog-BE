package com.haem.blogbackend.category.domain;

import com.haem.blogbackend.global.error.exception.base.NotFoundException;

public class CategoryNotFoundException extends NotFoundException {
    public CategoryNotFoundException(Long id) {
        super(String.format("존재하지 않는 카테고리입니다. id = %d", id));
    }
}
