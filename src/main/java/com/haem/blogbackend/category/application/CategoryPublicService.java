package com.haem.blogbackend.category.application;

import com.haem.blogbackend.category.application.dto.CategoryPostCountResult;
import com.haem.blogbackend.category.application.dto.CategorySummaryResult;
import com.haem.blogbackend.category.domain.CategoryRepository;
import com.haem.blogbackend.post.domain.PostRepository;
import com.haem.blogbackend.post.domain.PostNotFoundException;
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

    public List<CategorySummaryResult> getCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(CategorySummaryResult::from)
                .toList();
    }

    public long getPostCountByCategoryId(Long categoryId) {
        return postRepository.countByCategoryIdAndDeletedAtIsNull(categoryId);
    }

    public CategorySummaryResult getCategoryByPostId(Long postId) {
        var post = postRepository.findByIdAndDeletedAtIsNull(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        if (post.getCategory() == null) {
            return CategorySummaryResult.uncategorized();
        }

        return CategorySummaryResult.from(post.getCategory());
    }

    public List<CategoryPostCountResult> getCategoryPostCounts() {
        return categoryRepository.findCategoryPostCounts()
                .stream()
                .map(CategoryPostCountResult::from)
                .toList();
    }
}
