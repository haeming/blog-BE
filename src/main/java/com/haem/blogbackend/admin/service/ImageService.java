package com.haem.blogbackend.admin.service;

import com.haem.blogbackend.admin.component.FileManagement;
import com.haem.blogbackend.admin.repository.ImageRepository;
import com.haem.blogbackend.common.component.FileValidationComponent;
import com.haem.blogbackend.common.enums.BasePath;
import com.haem.blogbackend.domain.Image;
import com.haem.blogbackend.domain.Post;
import com.haem.blogbackend.common.exception.base.FileStorageException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Service
public class ImageService {
    private final FileManagement fileManagement;
    private final ImageRepository imageRepository;
    private final FileValidationComponent fileValidationComponent;

    public ImageService(
            FileManagement fileManagement,
            ImageRepository imageRepository,
            FileValidationComponent fileValidationComponent){
        this.fileManagement = fileManagement;
        this.imageRepository = imageRepository;
        this.fileValidationComponent = fileValidationComponent;
    }

    public void saveImage(Post post, MultipartFile file, BasePath basePath) {
        fileValidationComponent.validate(file, fileValidationComponent.isEmpty, "업로드할 파일이 비어있습니다.");
        fileValidationComponent.validate(file, fileValidationComponent.isImage.negate(), "이미지 파일 형식만 업로드할 수 있습니다.");

        try (InputStream inputStream = file.getInputStream()) {
            String originalName = file.getOriginalFilename();
            String imageUrl = fileManagement.uploadFile(inputStream, originalName, basePath);

            Image image = new Image(post, imageUrl, originalName);
            post.addImage(image);
            imageRepository.save(image);
        } catch (IOException e) {
            log.error("이미지 저장 실패", e);
            throw new FileStorageException("이미지 저장 중 오류가 발생했습니다.", e);
        }
    }

    public String uploadTempImage(MultipartFile file, BasePath basePath) {
        fileValidationComponent.validate(file, fileValidationComponent.isEmpty, "업로드할 파일이 비어있습니다.");
        fileValidationComponent.validate(file, fileValidationComponent.isImage.negate(), "이미지 파일 형식만 업로드할 수 있습니다.");

        try (InputStream inputStream = file.getInputStream()) {
            String originalName = file.getOriginalFilename();
            return fileManagement.uploadFile(inputStream, originalName, basePath);
        } catch (IOException e) {
            log.error("임시 이미지 업로드 실패", e);
            throw new FileStorageException("임시 이미지 업로드 중 오류가 발생했습니다.", e);
        }
    }

    public void deleteImage(Image image) {
        if (image == null || image.getImageUrl() == null) return;

        try {
            fileManagement.deleteFile(image.getImageUrl());
            imageRepository.delete(image);
        } catch (Exception e) {
            log.error("이미지 삭제 실패", e);
            throw new FileStorageException("이미지 삭제 중 오류가 발생했습니다.", e);
        }
    }
}
