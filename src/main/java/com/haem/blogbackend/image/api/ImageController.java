package com.haem.blogbackend.image.api;

import java.util.List;

import com.haem.blogbackend.global.common.enums.BasePath;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.haem.blogbackend.image.application.ImageService;

@RestController
@RequestMapping("/api/admin/images")
public class ImageController {
    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/temp-image")
    public List<String> uploadTempImages(@RequestPart("file") MultipartFile file){
        String url = imageService.uploadTempImage(file, BasePath.POST);
        return List.of(url);
    }

}
