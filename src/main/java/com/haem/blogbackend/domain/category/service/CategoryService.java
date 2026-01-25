package com.haem.blogbackend.domain.category.service;

import com.haem.blogbackend.admin.component.DirectoryManagement;
import com.haem.blogbackend.admin.component.EntityFinder;
import com.haem.blogbackend.admin.component.FileManagement;
import com.haem.blogbackend.admin.dto.request.CategoryCreateRequestDto;
import com.haem.blogbackend.admin.dto.request.CategoryUpdateNameRequestDto;
import com.haem.blogbackend.admin.dto.response.CategoryResponseDto;
import com.haem.blogbackend.common.enums.BasePath;
import com.haem.blogbackend.common.exception.base.FileStorageException;
import com.haem.blogbackend.common.exception.notfound.CategoryNotFoundException;
import com.haem.blogbackend.domain.category.dto.CategoryPostCountView;
import com.haem.blogbackend.domain.category.entity.Category;
import com.haem.blogbackend.domain.category.repository.CategoryRepository;
import com.haem.blogbackend.domain.post.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    public List<CategoryPostCountView> getCategoryPostCounts() {
        return categoryRepository.findCategoryPostCounts();
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

        // 이미지 파일 수정 없으면 그대로 반환
        if (file == null || file.isEmpty()) {
            return CategoryResponseDto.from(category);
        }

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
        // 이미지 없으면 null로 반환
        if(file == null || file.isEmpty()){
            return new UploadResult(null, null);
        }

        try (InputStream inputStream = file.getInputStream()) {
            String originalName = file.getOriginalFilename();
            String imageUrl = fileManagement.uploadFile(inputStream, originalName, BasePath.CATEGORY);
            return new UploadResult(imageUrl, originalName);
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
