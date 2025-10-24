package com.haem.blogbackend.util;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

public class FilePathGenerator {
    private FilePathGenerator() {}

    public static Path generate (String baseDir, String subDir){
        LocalDate now = LocalDate.now();
        String datePath = String.format("%s/%d/%02d/%02d", subDir, now.getYear(), now.getMonthValue(), now.getDayOfMonth());

        return Paths.get(baseDir, datePath);
    }
}
