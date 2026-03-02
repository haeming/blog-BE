package com.haem.blogbackend.category.api;

import com.haem.blogbackend.category.api.dto.CategoryPostCountResponseDto;
import com.haem.blogbackend.category.api.dto.CategoryResponseDto;
import com.haem.blogbackend.category.application.CategoryPublicService;
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
        return categoryPublicService.getCategories();
    }

    @GetMapping("/{categoryId}/post-count")
    public long getPostCountByCategoryId(@PathVariable("categoryId") Long categoryId) {
        return categoryPublicService.getPostCountByCategoryId(categoryId);
    }

    @GetMapping("/post-counts")
    public List<CategoryPostCountResponseDto> getCategoryPostCounts() {
        return categoryPublicService.getCategoryPostCounts()
                .stream()
                .map(CategoryPostCountResponseDto::from)
                .toList();
    }
}
