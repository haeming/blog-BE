package com.haem.blogbackend.service;

import com.haem.blogbackend.exception.base.FileStorageException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.UUID;

@Slf4j
@Service
public class FileStorageService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    private void cleanUpEmptyDirectory(Path dir, String basePath){
        try {
            Path baseDir = Paths.get(uploadDir, basePath);
            Path currentDir = dir;

            while (currentDir != null && currentDir.startsWith(baseDir)){
                if(Files.exists(currentDir) && Files.isDirectory(currentDir) && Files.list(currentDir).findAny().isEmpty()){
                    Files.delete(currentDir);
                    currentDir = currentDir.getParent();
                } else {
                    break;
                }
            }
        } catch (IOException e){
            log.warn("폴더 정리 실패: {}", e.getMessage());
        }
    }

    public String storeFile(MultipartFile file, String subDir) {
        if(file == null || file.isEmpty()){
            return null;
        }

        try {
            String originalName = file.getOriginalFilename();
            LocalDate now = LocalDate.now();
            String datePath = String.format("%s/%d/%02d/%02d", subDir, now.getYear(), now.getMonthValue(), now.getDayOfMonth());

            Path savePath = Paths.get(uploadDir, datePath);
            Files.createDirectories(savePath);

            String saveName = UUID.randomUUID() + "_" + originalName;
            Path filePath = savePath.resolve(saveName);

            file.transferTo(filePath.toFile());

            return "/uploadFiles/" + datePath + "/" + saveName;
        } catch (IOException e){
            throw new FileStorageException("파일 저장 중 오류가 발생했습니다.", e);
        }
    }

    public void deleteFile(String imageUrl, String basePath) {
        if(imageUrl != null){
            try {
                String relativePath = imageUrl.replace("/uploadFiles/", "");
                Path filePath = Paths.get(uploadDir, relativePath);

                Files.deleteIfExists(filePath);

                cleanUpEmptyDirectory(filePath.getParent(), basePath);
            } catch (IOException e){
                log.warn("파일 삭제 실패: {} (URL: {})", e.getMessage(), imageUrl);
            }
        }
    }
}
