package com.haem.blogbackend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ImageService {
    private final FileStorageService fileStorageService;

    public ImageService(FileStorageService fileStorageService){
        this.fileStorageService = fileStorageService;
    }

    public String saveImage(MultipartFile file, String subDir) throws IOException {
        return fileStorageService.storeFile(file, subDir);
    }

    public void deleteImage(String imageUrl) throws IOException {
        fileStorageService.deleteFile(imageUrl);
    }
}
