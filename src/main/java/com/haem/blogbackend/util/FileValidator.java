package com.haem.blogbackend.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public class FileValidator {
    private FileValidator() {}

    public static boolean isInValid(MultipartFile file) {
        return file == null || file.isEmpty();
    }
}
