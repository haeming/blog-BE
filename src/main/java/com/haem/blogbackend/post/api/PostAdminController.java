package com.haem.blogbackend.post.api;

import java.io.IOException;

import com.haem.blogbackend.post.application.dto.PostSummaryResult;
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

import com.haem.blogbackend.post.api.dto.PostCreateRequestDto;
import com.haem.blogbackend.post.api.dto.PostUpdateInfoRequestDto;
import com.haem.blogbackend.post.api.dto.PostResponseDto;
import com.haem.blogbackend.post.api.dto.PostSummaryResponseDto;
import com.haem.blogbackend.post.application.PostAdminService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/posts")
public class PostAdminController {
    private final PostAdminService postAdminService;

    public PostAdminController(PostAdminService postAdminService) {
        this.postAdminService = postAdminService;
    }

    @GetMapping
    public Page<PostSummaryResponseDto> getPosts(Pageable pageable){
        return postAdminService.getPosts(pageable)
                .map(PostSummaryResponseDto::from);
    }

    @GetMapping("/count")
    public Long getPostCount(){
        return postAdminService.getPostCount();
    }

    @GetMapping("/{id}")
    public PostResponseDto getPost(@PathVariable("id") Long id){
        return PostResponseDto.from(postAdminService.getPost(id));
    }

    @PostMapping
    public PostResponseDto createPost(
            @AuthenticationPrincipal UserDetails user,
            @Valid @RequestPart("data")PostCreateRequestDto requestDto,
            @RequestPart(value = "file", required=false) MultipartFile[] files
    ) throws IOException {
        String accountName = user.getUsername();
        return postAdminService.createPost(accountName, requestDto, files);
    }

    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable("id") Long id){
        postAdminService.deletePost(id);
    }

    @PatchMapping("/{id}/info")
    public PostResponseDto updatePostInfo(
            @PathVariable("id") Long id,
            @Valid @RequestBody PostUpdateInfoRequestDto requestDto
    ){
        return postAdminService.updatePostInfo(id, requestDto);
    }
}
