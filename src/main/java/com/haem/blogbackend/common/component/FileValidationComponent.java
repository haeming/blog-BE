package com.haem.blogbackend.common.component;

import com.haem.blogbackend.common.exception.base.InvalidFileException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.function.Predicate;

@Component
public class FileValidationComponent {
    public final Predicate<MultipartFile> isEmpty = MultipartFile::isEmpty;
    public final Predicate<MultipartFile> isImage = file -> {
        if(file.getContentType() == null || file.getContentType().equals("")){
            return false;
        }
        return file.getContentType().startsWith("image");
    };

    public Predicate<MultipartFile> hasAllowedExtension(String... extensions){
        return file -> {
            String originalFilename = file.getOriginalFilename();
            if(originalFilename == null){
                return false;
            };
            String ext = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            return Arrays.asList(extensions).contains(ext.toLowerCase());
        };
    }

    public void validate(MultipartFile file, Predicate<MultipartFile> predicate, String errorMessage){
        if(predicate.test(file)){
            throw new InvalidFileException(errorMessage);
        }
    }
}
