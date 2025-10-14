package com.haem.blogbackend.exception.notfound;

import com.haem.blogbackend.exception.base.NotFoundException;

public class CategoryNotFoundException extends NotFoundException{
    public CategoryNotFoundException(Long id) {
        super("존재하지 않는 카테고리입니다. id=" + id);
    }
}
