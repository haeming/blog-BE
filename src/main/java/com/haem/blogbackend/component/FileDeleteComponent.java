package com.haem.blogbackend.component;

import com.haem.blogbackend.util.DirectoryCleaner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Component
public class FileDeleteComponent {
    @Value("${file.upload-dir}")
    private String uploadDir;

    public void deleteFile(String imageUrl, String basePath) {
        if(imageUrl != null){
            try {
                String relativePath = imageUrl.replace("/uploadFiles/", "");
                Path filePath = Paths.get(uploadDir, relativePath);

                Files.deleteIfExists(filePath);

                DirectoryCleaner.clean(filePath.getParent(), Paths.get(uploadDir, basePath));
            } catch (IOException e){
                log.warn("파일 삭제 실패 (URL: {})", imageUrl, e);
            }
        }
    }
}
