package com.haem.blogbackend.component;

import com.haem.blogbackend.exception.base.FileStorageException;
import com.haem.blogbackend.util.FileNameGenerator;
import com.haem.blogbackend.util.FilePathGenerator;
import com.haem.blogbackend.util.FilePathUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@Component
public class FileUploadComponent {

    @Value("${file.upload-dir}")
    private String uploadDir;

    public String uploadFile(MultipartFile file, String subDir) {
        if(file == null || file.isEmpty()){
            return null;
        }

        try {
            String originalName = file.getOriginalFilename();

            Path saveFilePath = FilePathGenerator.generate(uploadDir, subDir);
            Files.createDirectories(saveFilePath);

            String saveFileName = FileNameGenerator.generate(originalName);
            Path filePath = saveFilePath.resolve(saveFileName);

            file.transferTo(filePath.toFile());

            return "/uploadFiles/" + saveFilePath + "/" + saveFileName;
        } catch (IOException e){
            throw new FileStorageException("파일 저장 중 오류가 발생했습니다.", e);
        }
    }
}
