package com.haem.blogbackend.admin.controller;

import java.io.IOException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.haem.blogbackend.admin.dto.request.PostCreateRequestDto;
import com.haem.blogbackend.admin.dto.request.PostUpdateInfoRequestDto;
import com.haem.blogbackend.admin.dto.response.PostResponseDto;
import com.haem.blogbackend.admin.dto.response.PostSummaryResponseDto;
import com.haem.blogbackend.admin.service.PostService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/posts")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public Page<PostSummaryResponseDto> getPosts(Pageable pageable){
        Page<PostSummaryResponseDto> postSummaryResponseDtoList = postService.getPosts(pageable);
        return postSummaryResponseDtoList;
    }

    @GetMapping("/count")
    public Long getPostCount(){
        long count = postService.getPostCount();
        return count;
    }

    @GetMapping("/{id}")
    public PostResponseDto getPost(@PathVariable("id") Long id){
        PostResponseDto responseDto = postService.getPost(id);
        return responseDto;
    }

    @PostMapping
    public PostResponseDto createPost(
            @AuthenticationPrincipal UserDetails user,
            @Valid @RequestPart("data")PostCreateRequestDto requestDto,
            @RequestPart(value = "file", required=false) MultipartFile[] files
    ) throws IOException {
        String accountName = user.getUsername();
        PostResponseDto postResponseDto = postService.createPost(accountName, requestDto, files);
        return postResponseDto;
    }

    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable("id") Long id){
        postService.deletePost(id);
    }

    @PatchMapping("/{id}/info")
    public PostResponseDto updatePostInfo(
            @PathVariable("id") Long id,
            @Valid @RequestBody PostUpdateInfoRequestDto requestDto
    ){
        PostResponseDto responseDto = postService.updatePostInfo(id, requestDto);
        return responseDto;
    }
}
