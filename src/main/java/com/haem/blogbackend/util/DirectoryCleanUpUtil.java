package com.haem.blogbackend.util;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
public class DirectoryCleanUpUtil {
    private DirectoryCleanUpUtil() {}

    public static void cleanUpEmptyDirectory(Path dir, Path baseDir) {
        try{
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
            log.warn("폴더 정리 실패", e);
        }
    }
}
