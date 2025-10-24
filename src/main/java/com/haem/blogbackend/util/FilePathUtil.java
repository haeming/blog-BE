package com.haem.blogbackend.util;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.UUID;

public class FilePathUtil {
    private FilePathUtil() {}

    public static Path saveFilePath(String baseDir, String subDir){
        LocalDate now = LocalDate.now();
        String datePath = String.format("%s/%d/%02d/%02d", subDir, now.getYear(), now.getMonthValue(), now.getDayOfMonth());

        return Paths.get(baseDir, datePath);
    }

    public static String saveFileName(String originalName){
        return UUID.randomUUID() + "_" + originalName;
    }
}
