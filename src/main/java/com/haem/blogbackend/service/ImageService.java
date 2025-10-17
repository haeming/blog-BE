package com.haem.blogbackend.service;

import com.haem.blogbackend.domain.Image;
import com.haem.blogbackend.domain.Post;
import com.haem.blogbackend.repository.ImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ImageService {
    private final FileStorageService fileStorageService;
    private final ImageRepository imageRepository;

    public ImageService(FileStorageService fileStorageService, ImageRepository imageRepository){
        this.fileStorageService = fileStorageService;
        this.imageRepository = imageRepository;
    }

    public void saveImage(Post post, MultipartFile file, String subDir) throws IOException {
        if(file == null || file.isEmpty()){
            return;
        }
        String imageUrl = fileStorageService.storeFile(file, subDir);
        Image image = new Image(post, imageUrl, file.getOriginalFilename());
        post.addImage(image);
    }

    public void deleteImage(Image image, String basePath) throws IOException {
        if(image == null || image.getImageUrl() == null){
            return;
        }
        fileStorageService.deleteFile(image.getImageUrl(), basePath);
        imageRepository.delete(image);
    }
}
