package com.haem.blogbackend.service;

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
import com.haem.blogbackend.repository.AdminRepository;
import com.haem.blogbackend.repository.CategoryRepository;
import com.haem.blogbackend.repository.PostRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional(readOnly = true)
@Service
public class PostService {
    private final PostRepository postRepository;
    private final AdminRepository adminRepository;
    private final CategoryRepository categoryRepository;

    public PostService(PostRepository postRepository, AdminRepository adminRepository, CategoryRepository categoryRepository) {
        this.postRepository = postRepository;
        this.adminRepository = adminRepository;
        this.categoryRepository = categoryRepository;
    }

    public long getPostCount(){
        return postRepository.count();
    }

    public Page<PostSummaryResponseDto> getPosts(Pageable pageable){
        return postRepository.findAll(pageable).map(PostSummaryResponseDto::new);
    }

    @Transactional
    public PostResponseDto createPost (String accountName, PostCreateRequestDto requestDto){
        Admin admin = adminRepository.findByAccountName(accountName).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 계정입니다."));
        Category category = categoryRepository.findById(requestDto.getCategoryId()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다."));
        Post post = new Post(category, admin, requestDto.getTitle(), requestDto.getContent());
        Post saved = postRepository.save(post);
        return new PostResponseDto(saved);
    }
}
