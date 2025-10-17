package com.haem.blogbackend.service;

import com.haem.blogbackend.dto.request.CategoryUpdateImageRequestDto;
import com.haem.blogbackend.dto.request.CategoryUpdateNameRequestDto;
import com.haem.blogbackend.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.haem.blogbackend.domain.Category;
import com.haem.blogbackend.dto.request.CategoryCreateRequestDto;
import com.haem.blogbackend.dto.response.CategoryResponseDto;
import com.haem.blogbackend.exception.base.FileStorageException;
import com.haem.blogbackend.exception.notfound.CategoryNotFoundException;
import com.haem.blogbackend.repository.CategoryRepository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Slf4j
@Transactional(readOnly = true)
@Service
public class CategoryService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    private final CategoryRepository categoryRepository;
    private final PostRepository postRepository;

    public CategoryService(CategoryRepository categoryRepository, PostRepository postRepository) {
        this.categoryRepository = categoryRepository;
        this.postRepository = postRepository;
    }

    public long getCategoryCount(){
        return categoryRepository.count();
    }

    private void cleanUpEmptyDirectories(Path dir) {
        try {
            while (dir != null && dir.startsWith(Paths.get(uploadDir, "blog", "category"))) {
                if (Files.exists(dir) && Files.isDirectory(dir) &&
                        Files.list(dir).findAny().isEmpty()) {
                    Files.delete(dir);
                    dir = dir.getParent();
                } else {
                    break;
                }
            }
        } catch (IOException e) {
            log.warn("폴더 정리 실패: {}", e.getMessage());
        }
    }

    @Transactional
    public CategoryResponseDto createCategory(CategoryCreateRequestDto requestDto, MultipartFile file) throws IOException {
        String imageUrl = null;
        String originalName = null;

        if(file != null && !file.isEmpty()){
            originalName = file.getOriginalFilename();

            LocalDate now = LocalDate.now();
            String datePath = String.format("blog/category/%d/%02d/%02d", now.getYear(), now.getMonthValue(), now.getDayOfMonth());
            Path savePath = Paths.get(uploadDir, datePath);
            Files.createDirectories(savePath);

            String saveName = UUID.randomUUID() + "_" + originalName;
            Path filePath = savePath.resolve(saveName);

            try{
                file.transferTo(filePath.toFile());
            } catch (IOException e){
                throw new FileStorageException("파일 저장 중 오류가 발생했습니다.", e);
            }
            imageUrl = "/uploadFiles/" + datePath + "/" + saveName;
        }

        Category category = Category.create(requestDto.getCategoryName(), imageUrl, originalName);
        Category saved = categoryRepository.save(category);
        return CategoryResponseDto.from(saved);
    }

    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));

        if (category.getImageUrl() != null) {
            try {
                String relativePath = category.getImageUrl().replace("/uploadFiles/", "");
                Path filePath = Paths.get(uploadDir, relativePath);

                Files.deleteIfExists(filePath);
                cleanUpEmptyDirectories(filePath.getParent());
            } catch (IOException e) {
                throw new FileStorageException("카테고리 이미지 삭제 중 오류가 발생했습니다.", e);
            }
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

        if (category.getImageUrl() != null) {
            try {
                String relativePath = category.getImageUrl().replace("/uploadFiles/", "");
                Path oldFilePath = Paths.get(uploadDir, relativePath);
                Files.deleteIfExists(oldFilePath);
            } catch (IOException e) {
                throw new FileStorageException("기존 이미지 파일 삭제 중 오류가 발생했습니다.", e);
            }
        }

        String imageUrl = null;
        String originalName = null;

        if (file != null && !file.isEmpty()) {
            originalName = file.getOriginalFilename();

            LocalDate now = LocalDate.now();
            String datePath = String.format("blog/category/%d/%02d/%02d",
                    now.getYear(), now.getMonthValue(), now.getDayOfMonth());
            Path savePath = Paths.get(uploadDir, datePath);
            try {
                Files.createDirectories(savePath);
                String saveName = UUID.randomUUID() + "_" + originalName;
                Path newFilePath = savePath.resolve(saveName);
                file.transferTo(newFilePath.toFile());
                imageUrl = "/uploadFiles/" + datePath + "/" + saveName;
            } catch (IOException e) {
                throw new FileStorageException("새 이미지 파일 저장 중 오류가 발생했습니다.", e);
            }
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
