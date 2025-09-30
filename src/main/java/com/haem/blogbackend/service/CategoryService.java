package com.haem.blogbackend.service;

import com.haem.blogbackend.domain.Category;
import com.haem.blogbackend.dto.request.CategoryCreateRequestDto;
import com.haem.blogbackend.dto.response.CategoryResponseDto;
import com.haem.blogbackend.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        Category category = new Category(requestDto.getCategoryName());
        Category saved = categoryRepository.save(category);
        return new CategoryResponseDto(saved);
    }

    @Transactional
    public void deleteCategory(long id){
        if (!categoryRepository.existsById(id)) {
            throw new IllegalArgumentException("존재하지 않는 카테고리입니다.");
        }
        categoryRepository.deleteById(id);
    }

}
