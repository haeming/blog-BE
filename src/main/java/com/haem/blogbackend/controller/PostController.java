package com.haem.blogbackend.controller;

import com.haem.blogbackend.dto.response.PostSummaryResponseDto;
import com.haem.blogbackend.service.PostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/post")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/allCount")
    public Map<String, Object> allCount(){
        Map<String, Object> map = new HashMap();
        map.put("result", postService.getPostCount());
        return map;
    }

    @GetMapping("/list")
    public Page<PostSummaryResponseDto> getPosts(Pageable pageable){
        return postService.getPosts(pageable);
    }
}
