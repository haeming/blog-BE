package com.haem.blogbackend.admin.service;

import com.haem.blogbackend.admin.component.DirectoryManagement;
import com.haem.blogbackend.admin.component.FileManagement;
import com.haem.blogbackend.admin.repository.CategoryRepository;
import com.haem.blogbackend.admin.repository.PostRepository;
import com.haem.blogbackend.domain.BasePath;
import com.haem.blogbackend.dto.request.CategoryUpdateNameRequestDto;
import com.haem.blogbackend.common.exception.base.FileStorageException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.haem.blogbackend.domain.Category;
import com.haem.blogbackend.dto.request.CategoryCreateRequestDto;
import com.haem.blogbackend.dto.response.CategoryResponseDto;
import com.haem.blogbackend.common.exception.notfound.CategoryNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
@Transactional(readOnly = true)
@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final PostRepository postRepository;
    private final FileManagement fileManagement;
    private final DirectoryManagement directoryManagement;

    public CategoryService(
            CategoryRepository categoryRepository,
            PostRepository postRepository,
            FileManagement fileManagement,
            DirectoryManagement directoryManagement) {
        this.categoryRepository = categoryRepository;
        this.postRepository = postRepository;
        this.fileManagement = fileManagement;
        this.directoryManagement = directoryManagement;
    }

    public long getCategoryCount(){
        return categoryRepository.count();
    }

    @Transactional
    public CategoryResponseDto createCategory(CategoryCreateRequestDto requestDto, MultipartFile file) {
        String imageUrl = null;
        String originalName = null;
        if(file != null && !file.isEmpty()){
            try (InputStream inputStream = file.getInputStream()){
                originalName = file.getOriginalFilename();
                imageUrl = fileManagement.uploadFile(inputStream, originalName, BasePath.CATEGORY);
            } catch (IOException e){
                log.error("카테고리 이미지 업로드 실패", e);
                throw new FileStorageException("이미지 업로드 중 오류가 발생했습니다.", e);
            }
        }
        Category category = Category.create(requestDto.getCategoryName(), imageUrl, originalName);
        Category saved = categoryRepository.save(category);
        return CategoryResponseDto.from(saved);
    }

    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));

        if(category.getImageUrl() != null){
            fileManagement.deleteFile(category.getImageUrl());
            String relativePath = category.getImageUrl().replace("/uploadFiles/", "");
            Path filePath = Paths.get("uploadFiles", relativePath);
            directoryManagement.clean(filePath.getParent(), Paths.get("uploadFiles"));
        }

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

        if (category.getImageUrl() != null){
            fileManagement.deleteFile(category.getImageUrl());
        }

        String imageUrl;
        String originalName;

        try (InputStream inputStream = file.getInputStream()) {
            originalName = file.getOriginalFilename();
            imageUrl = fileManagement.uploadFile(inputStream, originalName, BasePath.CATEGORY);
        } catch (IOException e) {
            log.error("카테고리 이미지 수정 중 오류 발생", e);
            throw new FileStorageException("이미지 수정 중 오류가 발생했습니다.");
        }

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
