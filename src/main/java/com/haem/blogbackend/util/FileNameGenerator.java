package com.haem.blogbackend.util;

import java.util.UUID;

public class FileNameGenerator {
    private FileNameGenerator() {}

    public static String generate (String originalName){
        return UUID.randomUUID() + "_" + originalName;
    }
}
