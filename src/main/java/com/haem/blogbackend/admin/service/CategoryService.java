package com.haem.blogbackend.admin.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.haem.blogbackend.admin.component.EntityFinder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.haem.blogbackend.admin.component.DirectoryManagement;
import com.haem.blogbackend.admin.component.FileManagement;
import com.haem.blogbackend.admin.repository.CategoryRepository;
import com.haem.blogbackend.admin.repository.PostRepository;
import com.haem.blogbackend.common.enums.BasePath;
import com.haem.blogbackend.common.exception.base.FileStorageException;
import com.haem.blogbackend.common.exception.notfound.CategoryNotFoundException;
import com.haem.blogbackend.domain.Category;
import com.haem.blogbackend.dto.request.CategoryCreateRequestDto;
import com.haem.blogbackend.dto.request.CategoryUpdateNameRequestDto;
import com.haem.blogbackend.dto.response.CategoryResponseDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional(readOnly = true)
@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final PostRepository postRepository;
    private final FileManagement fileManagement;
    private final DirectoryManagement directoryManagement;
    private final EntityFinder entityFinder;
    private record UploadResult(String originalName, String imageUrl) {}

    public CategoryService(
            CategoryRepository categoryRepository,
            PostRepository postRepository,
            FileManagement fileManagement,
            EntityFinder entityFinder,
            DirectoryManagement directoryManagement) {
        this.categoryRepository = categoryRepository;
        this.postRepository = postRepository;
        this.fileManagement = fileManagement;
        this.entityFinder = entityFinder;
        this.directoryManagement = directoryManagement;
    }

    public long getCategoryCount(){
        return categoryRepository.count();
    }

    @Transactional
    public CategoryResponseDto createCategory(CategoryCreateRequestDto requestDto, MultipartFile file) {
        
        UploadResult uploadResult = uploadImageFile(file);

        Category category = Category.create(requestDto.getCategoryName(), uploadResult.imageUrl, uploadResult.originalName);
        Category saved = categoryRepository.save(category);
        return CategoryResponseDto.from(saved);
    }

    @Transactional
    public void deleteCategory(Long id) {
        Category category = getCategoryOrThrow(id);
        deleteCategoryImage(category);
        categoryRepository.delete(category);
    }

    @Transactional
    public CategoryResponseDto updateCategoryName(Long id, CategoryUpdateNameRequestDto requestDto){
        Category category = getCategoryOrThrow(id);

        if(requestDto.getCategoryName() != null){
            category.updateName(requestDto.getCategoryName());
        }
        return CategoryResponseDto.from(category);
    }

    @Transactional
    public CategoryResponseDto updateCategoryImage(Long id, MultipartFile file) {
        Category category = getCategoryOrThrow(id);
        prepareCategoryImageUpdate(category, file);

        UploadResult uploadResult = uploadImageFile(file);

        category.updateImage(uploadResult.imageUrl, uploadResult.originalName);
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

    private Category getCategoryOrThrow(Long categoryId){
        return entityFinder.findByIdOrThrow(
                categoryId,
                categoryRepository,
                () -> new CategoryNotFoundException(categoryId)
        );
    }

    private UploadResult uploadImageFile(MultipartFile file){
        if(file == null || file.isEmpty()){
            return null;
        }

        try (InputStream inputStream = file.getInputStream()) {
            String originalName = file.getOriginalFilename();
            String imageUrl = fileManagement.uploadFile(inputStream, originalName, BasePath.CATEGORY);
            return new UploadResult(originalName, imageUrl);
        } catch (IOException e) {
            log.error("이미지 업로드 실패", e);
            throw new FileStorageException("이미지 업로드 중 오류가 발생했습니다.", e);
        }
    }

    private void deleteCategoryImage(Category category){
        if(category.getImageUrl() == null) {
            return;
        }

        fileManagement.deleteFile(category.getImageUrl());
        String relativePath = category.getImageUrl().replace("/uploadFiles/", "");
        Path filePath = Paths.get("uploadFiles", relativePath);
        directoryManagement.deleteEmptyParentDirectories(filePath.getParent(), Paths.get("uploadFiles"));
    }

    private void prepareCategoryImageUpdate(Category category, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return;
        }
        if (category.getImageUrl() != null) {
            fileManagement.deleteFile(category.getImageUrl());
        }
    }
}
