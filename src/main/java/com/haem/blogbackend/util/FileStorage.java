package com.haem.blogbackend.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

public class FileStorage {
    private FileStorage() {}

    public static void saveFile(MultipartFile file, Path filePath) throws IOException {
        file.transferTo(filePath.toFile());
    }
}
