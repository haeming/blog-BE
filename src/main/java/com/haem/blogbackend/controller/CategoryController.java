package com.haem.blogbackend.controller;

import com.haem.blogbackend.dto.request.CategoryCreateRequestDto;
import com.haem.blogbackend.dto.response.CategoryResponseDto;
import com.haem.blogbackend.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) { this.categoryService = categoryService; }

    @GetMapping("/allCount")
    public Map<String, Object> allCount(){
        Map<String, Object> map = new HashMap<>();
        map.put("result", categoryService.getCategoryCount());
        return map;
    }

    @PostMapping
    public CategoryResponseDto createCategory(@Valid @RequestBody CategoryCreateRequestDto requestDto){
        return categoryService.createCategory(requestDto);
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable("id") Long id){
        categoryService.deleteCategory(id);
    }
}
