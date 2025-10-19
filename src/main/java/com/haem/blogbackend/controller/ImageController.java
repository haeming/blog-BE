package com.haem.blogbackend.controller;

import com.haem.blogbackend.dto.response.ApiResponse;
import com.haem.blogbackend.service.ImageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/admin/images")
public class ImageController {
    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/temp-image")
    public ResponseEntity<ApiResponse<List<String>>> uploadTempImages(
            @RequestPart("file") MultipartFile file
    ) throws IOException {
        String url = imageService.uploadTempImage(file, "blog/post");
        return ResponseEntity.ok(ApiResponse.ok(List.of(url)));
    }

}
