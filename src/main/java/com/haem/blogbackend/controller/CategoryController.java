package com.haem.blogbackend.controller;

import com.haem.blogbackend.domain.Category;
import com.haem.blogbackend.dto.request.*;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.haem.blogbackend.dto.response.CategoryResponseDto;
import com.haem.blogbackend.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import com.haem.blogbackend.dto.request.CategoryCreateRequestDto;

import java.util.List;

@RestController
@RequestMapping("/api/admin/categories")
public class CategoryController {
    private final CategoryService categoryService;

public CategoryController(CategoryService categoryService) {
    this.categoryService = categoryService;
}

    @GetMapping("/count")
    public long getCategoryCount(){
        return categoryService.getCategoryCount();
    }

    @PostMapping
    public CategoryResponseDto createCategory(@Valid @RequestBody CategoryCreateRequestDto requestDto){
        return categoryService.createCategory(requestDto);
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable("id") Long id){
        categoryService.deleteCategory(id);
    }

    @PatchMapping("/{id}/name")
    public CategoryResponseDto updateCategoryName(@PathVariable("id") Long id, @Valid @RequestBody CategoryUpdateNameRequestDto requestDto){
        return categoryService.updateCategoryName(id, requestDto);
    }

    @PatchMapping("/{id}/image")
    public CategoryResponseDto updateCategoryImage(@PathVariable("id") Long id, @Valid @RequestBody CategoryUpdateImageRequestDto requestDto){
        return categoryService.updateCategoryImage(id, requestDto);
    }

    @GetMapping
    public List<CategoryResponseDto> getCategories(){
        return categoryService.getCategories();
    }
}
