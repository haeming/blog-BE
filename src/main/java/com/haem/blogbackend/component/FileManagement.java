package com.haem.blogbackend.component;

import com.haem.blogbackend.domain.BasePath;
import com.haem.blogbackend.exception.base.FileStorageException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.UUID;

@Slf4j
@Component
public class FileManagement {

    @Value("${file.upload-dir}")
    private String uploadDir;

    public String uploadFile(InputStream inputStream, String originalName, BasePath basePath) {
        validate(originalName, inputStream);

        try {
            LocalDate now = LocalDate.now();
            String datePath = String.format("%s/%d/%02d/%02d", basePath.getPath(), now.getYear(), now.getMonthValue(), now.getDayOfMonth());

            Path baseUploadDir = Paths.get(uploadDir).toAbsolutePath();
            Path savePath = baseUploadDir.resolve(datePath);
            Files.createDirectories(savePath);
            log.info("📁 [UPLOAD PATH] uploadDir={}, finalSavePath={}", uploadDir, savePath);

            String saveName = UUID.randomUUID() + "_" + originalName;
            Path filePath = savePath.resolve(saveName);
            log.info("🧭 Saving file to: {}", filePath.toAbsolutePath());
            log.info("📁 uploadDir={}, savePath={}", uploadDir, Paths.get(uploadDir, basePath.getPath()));
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);

            return "/uploadFiles/" + datePath + "/" + saveName;
        } catch (IOException e){
            throw new FileStorageException("파일 저장 중 오류가 발생했습니다.", e);
        }
    }

    public void deleteFile(String imageUrl) {
        if(imageUrl == null){
            return;
        }

        try {
            String relativePath = imageUrl.replace("/uploadFiles/", "");
            Path filePath = Paths.get(uploadDir, relativePath);
            Files.deleteIfExists(filePath);
        } catch (IOException e){
            log.warn("파일 삭제 실패 (URL: {})", imageUrl, e);
            throw new FileStorageException("파일 삭제 중 오류가 발생했습니다.", e);
        }
    }

    private void validate(String originalName, InputStream inputStream){
        if (originalName == null || originalName.isBlank()) {
            throw new IllegalArgumentException("파일 이름이 유효하지 않습니다.");
        }
        if (inputStream == null) {
            throw new IllegalArgumentException("파일 데이터가 없습니다.");
        }
    }
}