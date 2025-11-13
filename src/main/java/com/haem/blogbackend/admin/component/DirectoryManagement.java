package com.haem.blogbackend.admin.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@Component
public class DirectoryManagement {
    public void clean(Path dir, Path baseDir) {
        try {
            Path current = dir;

            while (current != null && current.startsWith(baseDir)) {
                if (Files.isDirectory(current)
                        && Files.exists(current)
                        && Files.list(current).findAny().isEmpty()) {
                    Files.delete(current);
                    current = current.getParent();
                } else {
                    break;
                }
            }
        } catch (IOException e) {
            log.warn("폴더 정리에 실패했습니다. ", e);
        }
    }
}
