package com.haem.blogbackend.service;

import com.haem.blogbackend.domain.Admin;
import com.haem.blogbackend.domain.Category;
import com.haem.blogbackend.domain.Post;
import com.haem.blogbackend.dto.request.PostCreateRequestDto;
import com.haem.blogbackend.dto.response.PostResponseDto;
import com.haem.blogbackend.dto.response.PostSummaryResponseDto;
import com.haem.blogbackend.repository.AdminRepository;
import com.haem.blogbackend.repository.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final AdminRepository adminRepository;

    public PostService(PostRepository postRepository, AdminRepository adminRepository) {
        this.postRepository = postRepository;
        this.adminRepository = adminRepository;
    }

    public long getPostCount(){
        return postRepository.count();
    }

    public Page<PostSummaryResponseDto> getPosts(Pageable pageable){
        return postRepository.findAll(pageable).map(PostSummaryResponseDto::new);
    }

    @Transactional
    public PostResponseDto createPost (PostCreateRequestDto requestDto, String accountName){
        Admin admin = adminRepository.findByAccountName(accountName).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 계정입니다."));
//        Category category =
        Post post = new Post(admin, requestDto.getTitle(), requestDto.getContent());
        Post saved = postRepository.save(post);
        return new PostResponseDto(saved);
    }
}
