package com.haem.blogbackend.service;

import com.haem.blogbackend.domain.Image;
import com.haem.blogbackend.exception.base.FileStorageException;
import com.haem.blogbackend.exception.notfound.PostNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.haem.blogbackend.domain.Admin;
import com.haem.blogbackend.domain.Category;
import com.haem.blogbackend.domain.Post;
import com.haem.blogbackend.dto.request.PostCreateRequestDto;
import com.haem.blogbackend.dto.response.PostResponseDto;
import com.haem.blogbackend.dto.response.PostSummaryResponseDto;
import com.haem.blogbackend.exception.notfound.AdminNotFoundException;
import com.haem.blogbackend.exception.notfound.CategoryNotFoundException;
import com.haem.blogbackend.repository.AdminRepository;
import com.haem.blogbackend.repository.CategoryRepository;
import com.haem.blogbackend.repository.PostRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Transactional(readOnly = true)
@Service
public class PostService {
    private final PostRepository postRepository;
    private final AdminRepository adminRepository;
    private final CategoryRepository categoryRepository;
    private final ImageService imageService;

    public PostService(
            PostRepository postRepository,
            AdminRepository adminRepository,
            CategoryRepository categoryRepository,
            ImageService imageService) {
        this.postRepository = postRepository;
        this.adminRepository = adminRepository;
        this.categoryRepository = categoryRepository;
        this.imageService = imageService;
    }

    public long getPostCount(){
        return postRepository.count();
    }

    public Page<PostSummaryResponseDto> getPosts(Pageable pageable){
        return postRepository.findAll(pageable).map(PostSummaryResponseDto::new);
    }

    @Transactional
    public PostResponseDto createPost (String accountName, PostCreateRequestDto requestDto, MultipartFile[] files) throws IOException {
        Admin admin = adminRepository.findByAccountName(accountName)
                .orElseThrow(() -> new AdminNotFoundException(accountName));
                
        Category category = categoryRepository.findById(requestDto.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException(requestDto.getCategoryId()));

        Post post = new Post(category, admin, requestDto.getTitle(), requestDto.getContent());
        Post saved = postRepository.save(post);

        if(files != null && files.length > 0){
            for(MultipartFile file : files){
                imageService.saveImage(saved, file, "blog/post");
            }
        }
        return new PostResponseDto(saved);
    }

    @Transactional
    public void deletePost (Long id){
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));

        for(Image image : post.getImages()){
            try {
                imageService.deleteImage(image, "blog/post");
            } catch (IOException e){
                throw new FileStorageException("포스트 이미지 삭제 중 에러가 발생했습니다.", e);
            }
        }
        postRepository.delete(post);
    }
}
