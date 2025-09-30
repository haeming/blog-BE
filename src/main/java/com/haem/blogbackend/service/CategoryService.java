package com.haem.blogbackend.service;

import com.haem.blogbackend.repository.CategoryRepository;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public long getCategoryCount(){
        return categoryRepository.count();
    }

}
