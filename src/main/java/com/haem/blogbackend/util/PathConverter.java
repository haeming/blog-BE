package com.haem.blogbackend.util;

import java.nio.file.Path;

public class PathConverter {

    private PathConverter() {}

    public static String pathToUrl(Path saveFilePath, String saveFileName){
        return "/uploadFiles/" + saveFilePath.toString().replace("\\", "/") + "/" + saveFileName;
    }
}
