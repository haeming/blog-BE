package com.haem.blogbackend.controller;

import com.haem.blogbackend.dto.request.PostCreateRequestDto;
import com.haem.blogbackend.dto.response.PostResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.haem.blogbackend.dto.response.ApiResponse;
import com.haem.blogbackend.dto.response.PostSummaryResponseDto;
import com.haem.blogbackend.service.PostService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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

    @PostMapping
    public ResponseEntity<ApiResponse<PostResponseDto>> createPost(
            @AuthenticationPrincipal UserDetails user,
            @RequestPart("data")PostCreateRequestDto requestDto,
            @RequestPart(value = "file", required=false) MultipartFile[] files
    ) throws IOException {
        String accountName = user.getUsername();
        PostResponseDto postResponseDto = postService.createPost(accountName, requestDto, files);
        return ResponseEntity.ok(ApiResponse.ok(postResponseDto));
    }
}
