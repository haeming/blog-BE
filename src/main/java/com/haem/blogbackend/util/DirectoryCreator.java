package com.haem.blogbackend.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DirectoryCreator {
    private DirectoryCreator() {}

    public static void create(Path saveFilePath) throws IOException {
        Files.createDirectories(saveFilePath);
    }
}
