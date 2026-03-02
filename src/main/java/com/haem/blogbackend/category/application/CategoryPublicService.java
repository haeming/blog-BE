package com.haem.blogbackend.category.application;

import com.haem.blogbackend.category.api.dto.CategoryResponseDto;
import com.haem.blogbackend.category.domain.CategoryRepository;
import com.haem.blogbackend.category.infrastructure.CategoryPostCountView;
import com.haem.blogbackend.post.domain.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Transactional(readOnly = true)
@Service
public class CategoryPublicService {
    private final CategoryRepository categoryRepository;
    private final PostRepository postRepository;

    public CategoryPublicService(CategoryRepository categoryRepository, PostRepository postRepository) {
        this.categoryRepository = categoryRepository;
        this.postRepository = postRepository;
    }

    public List<CategoryResponseDto> getCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(CategoryResponseDto::from)
                .toList();
    }

    public long getPostCountByCategoryId(Long categoryId) {
        return postRepository.countByCategoryIdAndDeletedAtIsNull(categoryId);
    }

    public List<CategoryPostCountView> getCategoryPostCounts() {
        return categoryRepository.findCategoryPostCounts();
    }
}
