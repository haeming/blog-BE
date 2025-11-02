package com.haem.blogbackend.service;

import com.haem.blogbackend.component.FileDeleteComponent;
import com.haem.blogbackend.component.FileManagement;
import com.haem.blogbackend.component.FileUploadComponent;
import com.haem.blogbackend.domain.BasePath;
import com.haem.blogbackend.domain.Image;
import com.haem.blogbackend.domain.Post;
import com.haem.blogbackend.exception.base.FileStorageException;
import com.haem.blogbackend.repository.ImageRepository;
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

    public ImageService(FileManagement fileManagement, ImageRepository imageRepository){
        this.fileManagement = fileManagement;
        this.imageRepository = imageRepository;
    }

    public void saveImage(Post post, MultipartFile file, BasePath basePath) {
        if(file == null || file.isEmpty()){
            return;
        }
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
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("빈 파일은 업로드할 수 없습니다.");
        }

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
