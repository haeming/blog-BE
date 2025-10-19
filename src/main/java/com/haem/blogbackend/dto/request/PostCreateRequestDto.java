package com.haem.blogbackend.dto.request;

import com.haem.blogbackend.domain.Post;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
public class PostCreateRequestDto {

    @NotBlank(message = "제목 입력은 필수입니다.")
    @Size(max = 255, message = "제목은 255자 이하여야 합니다.")
    private String title;

    @NotBlank(message = "내용을 입력해 주세요.")
    private String content;

    private Long categoryId;
    private List<MultipartFile> files;

    protected PostCreateRequestDto(){}

    private PostCreateRequestDto(String title, String content, Long categoryId, List<MultipartFile> files){
        this.title = title;
        this.content = content;
        this.categoryId = categoryId;
        this.files = files;
    }

    public static PostCreateRequestDto from(Post post){
//        List<ImageRequestDto> images = post.getImages() != null ?
//                post.getImages().stream()
//                .map(image -> ImageRequestDto.builder()
//                        .imageUrl(image.getImageUrl())
//                        .originalName(image.getOriginalName())
//                        .build())
//                .toList()
//        :
//                new ArrayList<>();
        List<MultipartFile> files = new ArrayList<>();

        Long categoryId = null;
        if(post.getCategory() != null){
            categoryId = post.getCategory().getId();
        }

        return PostCreateRequestDto.builder()
                .title(post.getTitle())
                .content(post.getContent())
                .categoryId(categoryId)
                .files(files)
                .build();
    }
}
