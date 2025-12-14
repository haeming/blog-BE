package com.haem.blogbackend.admin.controller;

import com.haem.blogbackend.admin.service.CommentService;
import com.haem.blogbackend.admin.dto.request.AdminCommentCreateRequestDto;
import com.haem.blogbackend.admin.dto.response.CommentResponseDto;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/comments")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    public List<CommentResponseDto> getComments(Long postId) {
        return commentService.getCommentsByPostId(postId);
    }

    @GetMapping("/count")
    public long getCommentCount() {
        return commentService.getCommentCount();
    }

    @PostMapping
    public CommentResponseDto createComment (
            @AuthenticationPrincipal UserDetails admin,
            @Valid @RequestBody AdminCommentCreateRequestDto requestDto
            ){
        return commentService.createComment(admin.getUsername(), requestDto);
    }
}
