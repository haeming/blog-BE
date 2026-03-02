package com.haem.blogbackend.category.api;

import com.haem.blogbackend.category.api.dto.CategoryCreateRequestDto;
import com.haem.blogbackend.category.api.dto.CategoryUpdateNameRequestDto;
import com.haem.blogbackend.category.api.dto.CategoryPostCountResponseDto;
import com.haem.blogbackend.category.api.dto.CategoryResponseDto;
import com.haem.blogbackend.category.application.CategoryAdminService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/admin/categories")
public class CategoryAdminController {
    private final CategoryAdminService categoryAdminService;

public CategoryAdminController(CategoryAdminService categoryAdminService) {
    this.categoryAdminService = categoryAdminService;
}

    @GetMapping
    public List<CategoryResponseDto> getCategories(){
        return categoryAdminService.getCategories();
    }

    @GetMapping("/count")
    public long getCategoryCount(){
        return categoryAdminService.getCategoryCount();
    }

    @GetMapping("/{categoryId}/post-count")
    public long getPostCountByCategoryId(@PathVariable("categoryId") Long categoryId){
        return categoryAdminService.getPostCountByCategoryId(categoryId);
    }

    // 집계 조회는 View → Response 변환을 컨트롤러에서 수행
    @GetMapping("/post-counts")
    public List<CategoryPostCountResponseDto> getCategoryPostCounts() {
        return categoryAdminService.getCategoryPostCounts()
                .stream()
                .map(CategoryPostCountResponseDto::from)
                .toList();
    }

    @PostMapping
    public CategoryResponseDto createCategory(
            @Valid @RequestPart("data") CategoryCreateRequestDto requestDto,
            @RequestPart(value = "file", required = false) MultipartFile file
    ){
        return categoryAdminService.createCategory(requestDto, file);
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable("id") Long id){
        categoryAdminService.deleteCategory(id);
    }

    @PatchMapping("/{id}/name")
    public CategoryResponseDto updateCategoryName(
            @PathVariable("id") Long id,
            @Valid @RequestBody CategoryUpdateNameRequestDto requestDto
    ){
        return categoryAdminService.updateCategoryName(id, requestDto);
    }

    @PatchMapping("/{id}/image")
    public CategoryResponseDto updateCategoryImage(
            @PathVariable("id") Long id,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        return categoryAdminService.updateCategoryImage(id, file);
    }
}
