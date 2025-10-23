package com.haem.blogbackend.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.haem.blogbackend.dto.request.CategoryCreateRequestDto;
import com.haem.blogbackend.dto.request.CategoryUpdateNameRequestDto;
import com.haem.blogbackend.dto.response.CategoryResponseDto;
import com.haem.blogbackend.service.CategoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/categories")
public class CategoryController {
    private final CategoryService categoryService;

public CategoryController(CategoryService categoryService) {
    this.categoryService = categoryService;
}

    @GetMapping
    public List<CategoryResponseDto> getCategories(){
        List<CategoryResponseDto> categoryResponseDtoList = categoryService.getCategories();
        return categoryResponseDtoList;
    }

    @GetMapping("/count")
    public long getCategoryCount(){
        long count = categoryService.getCategoryCount();
        return count;
    }

    @GetMapping("/{categoryId}/post-count")
    public long getPostCountByCategoryId(@PathVariable("categoryId") Long categoryId){
        long count = categoryService.getPostCountByCategoryId(categoryId);
        return count;
    }

    @PostMapping
    public CategoryResponseDto createCategory(
            @Valid @RequestPart("data") CategoryCreateRequestDto requestDto,
            @RequestPart(value = "file", required = false) MultipartFile file
    )throws IOException{
        CategoryResponseDto responseDto = categoryService.createCategory(requestDto, file);
        return responseDto;
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable("id") Long id){
        categoryService.deleteCategory(id);
    }

    @PatchMapping("/{id}/name")
    public CategoryResponseDto updateCategoryName(
            @PathVariable("id") Long id,
            @Valid @RequestBody CategoryUpdateNameRequestDto requestDto
    ){
        CategoryResponseDto responseDto = categoryService.updateCategoryName(id, requestDto);
        return responseDto;
    }

    @PatchMapping("/{id}/image")
    public CategoryResponseDto updateCategoryImage(
            @PathVariable("id") Long id,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        CategoryResponseDto responseDto = categoryService.updateCategoryImage(id, file);
        return responseDto;
    }
}
