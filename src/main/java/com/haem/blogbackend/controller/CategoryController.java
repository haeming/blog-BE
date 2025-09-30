package com.haem.blogbackend.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.haem.blogbackend.dto.request.CategoryCreateRequestDto;
import com.haem.blogbackend.dto.request.CategoryUpdateRequestDto;
import com.haem.blogbackend.dto.response.CategoryCountResponseDto;
import com.haem.blogbackend.dto.response.CategoryResponseDto;
import com.haem.blogbackend.service.CategoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/categories")
public class CategoryController {
    private final CategoryService categoryService;


    @PostMapping
    public CategoryResponseDto createCategory(@Valid @RequestBody CategoryCreateRequestDto requestDto){
        return categoryService.createCategory(requestDto);
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable("id") Long id){
        categoryService.deleteCategory(id);
    }

    @PutMapping("/{id}")
    public CategoryResponseDto updateCategory(@PathVariable("id") Long id, @Valid @RequestBody CategoryUpdateRequestDto requestDto){
        return categoryService.updateCategory(id, requestDto);
    }
}
