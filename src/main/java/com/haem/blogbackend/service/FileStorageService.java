package com.haem.blogbackend.service;

import com.haem.blogbackend.exception.base.FileStorageException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    public String storeFile(MultipartFile file, String subDir) throws IOException {
        String originalName = file.getOriginalFilename();
        LocalDate now = LocalDate.now();
        String datePath = String.format("%s/%d/%02d/%02d", subDir, now.getYear(), now.getMonthValue(), now.getDayOfMonth());

        Path savePath = Paths.get(uploadDir, datePath);
        Files.createDirectories(savePath);

        String saveName = UUID.randomUUID() + "_" + originalName;
        Path filePath = savePath.resolve(saveName);

        try {
            file.transferTo(filePath.toFile());
        } catch (IOException e){
            throw new FileStorageException("파일 저장 중 오류가 발생했습니다.", e);
        }
        return "/uploadFiles/" + datePath + "/" + saveName;
    }

    public void deleteFile(String imageUrl) throws IOException {
        if(imageUrl != null){
            String relativePath = imageUrl.replace("/uploadFiles/", "");
            Path filePath = Paths.get(uploadDir, relativePath);
            Files.deleteIfExists(filePath);
        }
    }
}
