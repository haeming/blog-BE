package com.haem.blogbackend.global.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DirectoryManagement {
    public void deleteEmptyParentDirectories(Path dir, Path baseDir) {
        try {
            deleteEmptyParents(dir, baseDir);
        } catch (IOException e) {
            log.warn("폴더 정리에 실패했습니다. ", e);
        }
    }

    private void deleteEmptyParents(Path dir, Path baseDir) throws IOException {
        if (dir == null || !dir.startsWith(baseDir)) {
            return;
        }

        if (isDirectoryEmpty(dir)) {
            deleteDirectory(dir);
            deleteEmptyParents(dir.getParent(), baseDir);
        }
    }

    private void deleteDirectory(Path dir) throws IOException {
        Files.delete(dir);
    }

    private boolean isDirectoryEmpty(Path dir) throws IOException {
        return Files.isDirectory(dir)
                && Files.exists(dir)
                && Files.list(dir).findAny().isEmpty();
    }
}
