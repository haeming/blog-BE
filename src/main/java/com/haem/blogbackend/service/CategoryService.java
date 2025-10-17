package com.haem.blogbackend.service;

import com.haem.blogbackend.dto.request.CategoryUpdateNameRequestDto;
import com.haem.blogbackend.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.haem.blogbackend.domain.Category;
import com.haem.blogbackend.dto.request.CategoryCreateRequestDto;
import com.haem.blogbackend.dto.response.CategoryResponseDto;
import com.haem.blogbackend.exception.notfound.CategoryNotFoundException;
import com.haem.blogbackend.repository.CategoryRepository;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Transactional(readOnly = true)
@Service
public class CategoryService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    private static final String CATEGORY_BASE_PATH = "blog/category";

    private final CategoryRepository categoryRepository;
    private final PostRepository postRepository;
    private final FileStorageService fileStorageService;

    public CategoryService(CategoryRepository categoryRepository, PostRepository postRepository, FileStorageService fileStorageService) {
        this.categoryRepository = categoryRepository;
        this.postRepository = postRepository;
        this.fileStorageService = fileStorageService;
    }

    public long getCategoryCount(){
        return categoryRepository.count();
    }

    @Transactional
    public CategoryResponseDto createCategory(CategoryCreateRequestDto requestDto, MultipartFile file) {
        String originalName = (file != null && !file.isEmpty()) ? file.getOriginalFilename() : null;
        String imageUrl = fileStorageService.storeFile(file, CATEGORY_BASE_PATH);

        Category category = Category.create(requestDto.getCategoryName(), imageUrl, originalName);
        Category saved = categoryRepository.save(category);
        return CategoryResponseDto.from(saved);
    }

    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
        fileStorageService.deleteFile(category.getImageUrl(), CATEGORY_BASE_PATH);
        categoryRepository.delete(category);
    }

    @Transactional
    public CategoryResponseDto updateCategoryName(Long id, CategoryUpdateNameRequestDto requestDto){
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
        if(requestDto.getCategoryName() != null){
            category.updateName(requestDto.getCategoryName());
        }
        return CategoryResponseDto.from(category);
    }

    @Transactional
    public CategoryResponseDto updateCategoryImage(Long id, MultipartFile file) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));

        if (file == null || file.isEmpty()) {
            return CategoryResponseDto.from(category);
        }

        fileStorageService.deleteFile(category.getImageUrl(), CATEGORY_BASE_PATH);

        String originalName = file.getOriginalFilename();
        String imageUrl = fileStorageService.storeFile(file, CATEGORY_BASE_PATH);

        category.updateImage(imageUrl, originalName);
        return CategoryResponseDto.from(category);
    }

    public List<CategoryResponseDto> getCategories(){
        return categoryRepository.findAll()
                .stream()
                .map(CategoryResponseDto::from)
                .toList();
    }

    public long getPostCountByCategoryId(Long categoryId){
        return postRepository.countByCategoryId(categoryId);
    }
}
