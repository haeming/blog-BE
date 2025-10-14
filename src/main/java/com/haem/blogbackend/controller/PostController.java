package com.haem.blogbackend.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.haem.blogbackend.dto.response.ApiResponse;
import com.haem.blogbackend.dto.response.PostSummaryResponseDto;
import com.haem.blogbackend.service.PostService;

@RestController
@RequestMapping("/api/admin/posts")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<PostSummaryResponseDto>>> getPosts(Pageable pageable){
        Page<PostSummaryResponseDto> postSummaryResponseDtoList = postService.getPosts(pageable);
        return ResponseEntity.ok(ApiResponse.ok(postSummaryResponseDtoList));
    }

    @GetMapping("/count")
    public ResponseEntity<ApiResponse<Long>> getPostCount(){
        long count = postService.getPostCount();
        return ResponseEntity.ok(ApiResponse.ok(count));
    }
}
