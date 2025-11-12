package com.haem.blogbackend.common.enums;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum ImageExtension {
    JPG("jpg"),
    JPEG("jpeg"),
    PNG("png"),
    GIF("gif"),
    WEBP("webp");

    private final String extension;

    ImageExtension (String extension) {
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }

    private static final Set<String> ALLOWED_EXTENSIONS_SET =
            Arrays.stream(ImageExtension.values())
                    .map(ImageExtension::getExtension)
                    .collect(Collectors.toSet());

    public static boolean contains(String extension) {
        return ALLOWED_EXTENSIONS_SET.contains(extension.toLowerCase());
    }

    public static String getAllowedExtensionsString () {
        return Arrays.stream(ImageExtension.values())
                .map(ImageExtension::getExtension)
                .collect(Collectors.joining(","))
                .toUpperCase();
    }
}
