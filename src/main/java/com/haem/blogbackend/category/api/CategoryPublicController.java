package com.haem.blogbackend.category.api;

import com.haem.blogbackend.category.api.dto.CategoryPostCountResponseDto;
import com.haem.blogbackend.category.api.dto.CategoryResponseDto;
import com.haem.blogbackend.category.application.CategoryPublicService;
import com.haem.blogbackend.category.application.dto.CategoryPostCountResult;
import com.haem.blogbackend.category.application.dto.CategorySummaryResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryPublicController {
    private final CategoryPublicService categoryPublicService;

    public CategoryPublicController(CategoryPublicService categoryPublicService) {
        this.categoryPublicService = categoryPublicService;
    }

    @GetMapping
    public List<CategoryResponseDto> getCategories() {
        List<CategorySummaryResult> results = categoryPublicService.getCategories();
        return results.stream()
                .map(CategoryResponseDto::from)
                .toList();
    }

    @GetMapping("/{categoryId}/post-count")
    public long getPostCountByCategoryId(@PathVariable("categoryId") Long categoryId) {
        return categoryPublicService.getPostCountByCategoryId(categoryId);
    }

    @GetMapping("/post/{postId}")
    public CategoryResponseDto getCategoryByPostId(@PathVariable("postId") Long postId) {
        CategorySummaryResult result = categoryPublicService.getCategoryByPostId(postId);
        return CategoryResponseDto.from(result);
    }

    @GetMapping("/post-counts")
    public List<CategoryPostCountResponseDto> getCategoryPostCounts() {
        List<CategoryPostCountResult> results = categoryPublicService.getCategoryPostCounts();
        return results.stream()
                .map(CategoryPostCountResponseDto::from)
                .toList();
    }
}
