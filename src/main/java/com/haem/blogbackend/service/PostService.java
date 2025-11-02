package com.haem.blogbackend.service;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.haem.blogbackend.domain.Admin;
import com.haem.blogbackend.domain.Category;
import com.haem.blogbackend.domain.Image;
import com.haem.blogbackend.domain.Post;
import com.haem.blogbackend.dto.request.PostCreateRequestDto;
import com.haem.blogbackend.dto.request.PostUpdateInfoRequestDto;
import com.haem.blogbackend.dto.response.PostResponseDto;
import com.haem.blogbackend.dto.response.PostSummaryResponseDto;
import com.haem.blogbackend.exception.base.FileStorageException;
import com.haem.blogbackend.exception.notfound.AdminNotFoundException;
import com.haem.blogbackend.exception.notfound.CategoryNotFoundException;
import com.haem.blogbackend.exception.notfound.PostNotFoundException;
import com.haem.blogbackend.repository.AdminRepository;
import com.haem.blogbackend.repository.CategoryRepository;
import com.haem.blogbackend.repository.ImageRepository;
import com.haem.blogbackend.repository.PostRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional(readOnly = true)
@Service
public class PostService {
    private final PostRepository postRepository;
    private final AdminRepository adminRepository;
    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;
    private final ImageService imageService;

    public PostService(
            PostRepository postRepository,
            AdminRepository adminRepository,
            CategoryRepository categoryRepository,
            ImageRepository imageRepository,
            ImageService imageService) {
        this.postRepository = postRepository;
        this.adminRepository = adminRepository;
        this.categoryRepository = categoryRepository;
        this.imageRepository = imageRepository;
        this.imageService = imageService;
    }

    public long getPostCount(){
        return postRepository.count();
    }

    public Page<PostSummaryResponseDto> getPosts(Pageable pageable){
        return postRepository.findAll(pageable).map(post -> {
            String categoryName = Optional.ofNullable(post.getCategory())
                    .map(Category::getCategoryName)
                    .orElse("미분류");
            return PostSummaryResponseDto.from(post, categoryName);
        });
    }

    public PostResponseDto getPost (Long id){
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));
        return PostResponseDto.from(post);
    }

    @Transactional
    public PostResponseDto createPost(String accountName, PostCreateRequestDto requestDto, MultipartFile[] files) throws IOException {
        Admin admin = adminRepository.findByAccountName(accountName)
                .orElseThrow(() -> new AdminNotFoundException(accountName));

        Long categoryId = Optional.ofNullable(requestDto.getCategoryId()).orElse(0L);
        Category category = (categoryId != 0)
                ? categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId))
                : null;
        
        Post post = Post.create(category, admin, requestDto.getTitle(), requestDto.getContent());
        postRepository.save(post);

        Matcher matcher = Pattern.compile("\\(/uploadFiles[^)]+\\)").matcher(requestDto.getContent());
        while (matcher.find()) {
            String path = matcher.group().replace("(", "").replace(")", "");
            Image image = new Image(post, path, Paths.get(path).getFileName().toString());
            imageRepository.save(image);
        }

        return PostResponseDto.from(post);
    }


    @Transactional
    public void deletePost (Long id){
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));

        for(Image image : post.getImages()){
            try {
                imageService.deleteImage(image);
            } catch (Exception e){
                throw new FileStorageException("포스트 이미지 삭제 중 에러가 발생했습니다.", e);
            }
        }
        postRepository.delete(post);
    }

    @Transactional
    public PostResponseDto updatePostInfo(Long id, PostUpdateInfoRequestDto requestDto){
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));
        if(requestDto.getTitle() != null && requestDto.getContent() != null){
            post.updateInfo(requestDto.getTitle(), requestDto.getContent());
        }
        return PostResponseDto.from(post);
    }
}
