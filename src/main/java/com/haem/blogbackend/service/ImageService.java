package com.haem.blogbackend.service;

import com.haem.blogbackend.component.FileDeleteComponent;
import com.haem.blogbackend.component.FileUploadComponent;
import com.haem.blogbackend.domain.Image;
import com.haem.blogbackend.domain.Post;
import com.haem.blogbackend.repository.ImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ImageService {
    private final FileUploadComponent fileUploadComponent;
    private final FileDeleteComponent fileDeleteComponent;
    private final ImageRepository imageRepository;

    public ImageService(FileUploadComponent fileUploadComponent, FileDeleteComponent fileDeleteComponent, ImageRepository imageRepository){
        this.fileUploadComponent = fileUploadComponent;
        this.fileDeleteComponent = fileDeleteComponent;
        this.imageRepository = imageRepository;
    }

    public void saveImage(Post post, MultipartFile file, String subDir) throws IOException {
        if(file == null || file.isEmpty()){
            return;
        }
        String imageUrl = fileUploadComponent.uploadFile(file, subDir);
        Image image = new Image(post, imageUrl, file.getOriginalFilename());
        post.addImage(image);
    }

    public String uploadTempImage(MultipartFile file, String subDir) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("빈 파일은 업로드할 수 없습니다.");
        }
        return fileUploadComponent.uploadFile(file, subDir);
    }

    public void deleteImage(Image image, String basePath) throws IOException {
        if(image == null || image.getImageUrl() == null){
            return;
        }
        fileDeleteComponent.deleteFile(image.getImageUrl(), basePath);
        imageRepository.delete(image);
    }
}
