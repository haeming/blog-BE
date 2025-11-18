package com.haem.blogbackend.admin.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.haem.blogbackend.admin.component.FileManagement;
import com.haem.blogbackend.admin.repository.ImageRepository;
import com.haem.blogbackend.common.component.ImageValidator;
import com.haem.blogbackend.common.enums.BasePath;
import com.haem.blogbackend.common.enums.ImageExtension;
import com.haem.blogbackend.common.exception.base.FileStorageException;
import com.haem.blogbackend.common.exception.base.InvalidFileException;
import com.haem.blogbackend.domain.Image;
import com.haem.blogbackend.domain.Post;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ImageService {
    private final FileManagement fileManagement;
    private final ImageRepository imageRepository;
    private final List<ImageValidator> imageValidators;

    public ImageService(
            FileManagement fileManagement,
            ImageRepository imageRepository,
            List<ImageValidator> imageValidators){
        this.fileManagement = fileManagement;
        this.imageRepository = imageRepository;
        this.imageValidators = imageValidators;
    }

    public void saveImage(Post post, MultipartFile file, BasePath basePath) {
        validateImage(file);
        byte[] imageBytes = extractAndValidateBytes(file);
        String imageUrl = uploadBytes(imageBytes, file.getOriginalFilename(), basePath);
        saveImageEntity(post, imageUrl, file.getOriginalFilename());
    }

    public String uploadTempImage(MultipartFile file, BasePath basePath) {
        validateImage(file);
        byte[] imageBytes = extractAndValidateBytes(file);

        return uploadBytes(imageBytes, file.getOriginalFilename(), basePath);
    }

    public void deleteImage(Image image) {
        if (image == null || image.getImageUrl() == null) return;

        deleteFileAndEntity(image);
    }

    private byte[] extractAndValidateBytes(MultipartFile file){
        try (InputStream originalInputStream = file.getInputStream()){
            byte[] bytes = originalInputStream.readAllBytes();
            validateImageContent(bytes);
            return bytes;
        } catch (IOException e) {
            log.error("이미지 파일 읽기 실패", e);
            throw new FileStorageException("이미지 처리 중 오류가 발생했습니다.", e);

        }
    }

    private void validateImage(MultipartFile file) {
        if(file == null || file.isEmpty()){
            throw new InvalidFileException("업로드할 파일이 비어있습니다.");
        }

        String contentType = file.getContentType();

        if (contentType == null || !contentType.startsWith("image")) {
            throw new InvalidFileException("이미지 파일 형식만 업로드할 수 있습니다.");
        }

        String originalFileName = file.getOriginalFilename();
        if(originalFileName == null || originalFileName.isEmpty()){
            throw new InvalidFileException("파일명을 확인할 수 없습니다.");
        }

        String ext = originalFileName.substring(originalFileName.lastIndexOf(".")+1);
        if(!ImageExtension.contains(ext)){
            throw new InvalidFileException(String.format("허용되지 않는 파일 확장자입니다. %s 파일만 업로드할 수 있습니다.", ImageExtension.getAllowedExtensionsString()));
        }
    }

    private String uploadBytes(byte[] bytes, String originalName, BasePath basePath){
        InputStream stream = new ByteArrayInputStream(bytes);
        return fileManagement.uploadFile(stream, originalName, basePath);
    }

    private void saveImageEntity(Post post, String imageUrl, String originalName){
        Image image = new Image(post, imageUrl, originalName);
        post.addImage(image);
        imageRepository.save(image);
    }    

    private void deleteFileAndEntity(Image image) {
        fileManagement.deleteFile(image.getImageUrl());
        imageRepository.delete(image);
    }
    
    private void validateImageContent(byte[] imageBytes){
        boolean allContentValid = imageValidators.stream()
                    .anyMatch(validator -> validator.test(imageBytes));

        if(!allContentValid){
            throw new InvalidFileException("이미지 파일 내용이 유효하지 않아 검증에 실패했습니다.");
        }
    }
}
