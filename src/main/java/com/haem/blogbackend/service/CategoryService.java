package com.haem.blogbackend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.haem.blogbackend.domain.Category;
import com.haem.blogbackend.dto.request.CategoryCreateRequestDto;
import com.haem.blogbackend.dto.request.CategoryUpdateRequestDto;
import com.haem.blogbackend.dto.response.CategoryResponseDto;
import com.haem.blogbackend.exception.CategoryNotFoundException;
import com.haem.blogbackend.repository.CategoryRepository;

@Transactional(readOnly = true)
@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public long getCategoryCount(){
        return categoryRepository.count();
    }

    @Transactional
    public CategoryResponseDto createCategory(CategoryCreateRequestDto requestDto){
        Category category = Category.create(requestDto.getCategoryName());
        Category saved = categoryRepository.save(category);
        return new CategoryResponseDto(saved);
    }

    @Transactional
    public void deleteCategory(long id){
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new CategoryNotFoundException(id));
        
        categoryRepository.delete(category);
    }

    @Transactional
    public CategoryResponseDto updateCategory(Long id, CategoryUpdateRequestDto requestDto){
        Category category = categoryRepository.findById(id)
            .orElseThrow(() -> new CategoryNotFoundException(id));

        category.setCategoryName(requestDto.getCategoryName());

        return new CategoryResponseDto(category);
    }

}
