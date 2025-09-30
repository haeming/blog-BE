package com.haem.blogbackend.controller;

import com.haem.blogbackend.service.CategoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/category")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) { this.categoryService = categoryService; }

    @GetMapping("/allCount")
    public Map<String, Object> allCount(){
        Map<String, Object> map = new HashMap<>();
        map.put("result", categoryService.getCategoryCount());
        return map;
    }
}
