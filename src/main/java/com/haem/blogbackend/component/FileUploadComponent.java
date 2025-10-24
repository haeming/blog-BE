package com.haem.blogbackend.component;

import com.haem.blogbackend.exception.base.FileStorageException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.UUID;

@Slf4j
@Component
public class FileUploadComponent {

    @Value("${file.upload-dir}")
    private String uploadDir;

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
}
