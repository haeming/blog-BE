package com.haem.blogbackend.dto.request;

import com.haem.blogbackend.domain.Category;
import com.haem.blogbackend.domain.Image;
import com.haem.blogbackend.domain.Post;
import com.haem.blogbackend.dto.ImageRequestDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

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
    private List<ImageRequestDto> images;

    protected PostCreateRequestDto(){}

    private PostCreateRequestDto(String title, String content, Long categoryId, List<ImageRequestDto> images){
        this.title = title;
        this.content = content;
        this.categoryId = categoryId;
        this.images = images;
    }

    public static PostCreateRequestDto from(Post post){
        List<ImageRequestDto> images = post.getImages() != null ?
                post.getImages().stream()
                .map(image -> ImageRequestDto.builder()
                        .imageUrl(image.getImageUrl())
                        .originalName(image.getOriginalName())
                        .build())
                .toList()
        :
                new ArrayList<>();

        return PostCreateRequestDto.builder()
                .title(post.getTitle())
                .content(post.getContent())
                .categoryId(post.getCategory().getId())
                .images(images)
                .build();
    }
}
