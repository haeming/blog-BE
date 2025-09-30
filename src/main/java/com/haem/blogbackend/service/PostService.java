package com.haem.blogbackend.service;

import com.haem.blogbackend.dto.response.PostSummaryResponseDto;
import com.haem.blogbackend.repository.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PostService {
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public long getPostCount(){
        return postRepository.count();
    }

    public Page<PostSummaryResponseDto> getPosts(Pageable pageable){
        return postRepository.findAll(pageable).map(PostSummaryResponseDto::new);
    }
}
