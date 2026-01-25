package com.haem.blogbackend.post.api;

import com.haem.blogbackend.post.api.dto.PostResponseDto;
import com.haem.blogbackend.post.api.dto.PostSummaryResponseDto;
import com.haem.blogbackend.post.application.PostPublicService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/posts")
public class PostPublicController {
    private final PostPublicService postPublicService;

    public PostPublicController(PostPublicService postPublicService) {
        this.postPublicService = postPublicService;
    }

    @GetMapping
    public Page<PostSummaryResponseDto> getPosts(Pageable pageable) {
        return postPublicService.getPublicPosts(pageable)
                .map(PostSummaryResponseDto::from);
    }

    @GetMapping("/{id}")
    public PostResponseDto getPost(@PathVariable("id") Long id) {
        return PostResponseDto.from(postPublicService.getPublicPost(id));
    }
}
