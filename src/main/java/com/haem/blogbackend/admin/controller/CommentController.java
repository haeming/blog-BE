package com.haem.blogbackend.admin.controller;

import com.haem.blogbackend.admin.service.CommentService;
import com.haem.blogbackend.domain.Comment;
import com.haem.blogbackend.dto.request.CommentCreateRequestDto;
import com.haem.blogbackend.dto.response.CommentResponseDto;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/comments")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public CommentResponseDto createComment (
            @AuthenticationPrincipal UserDetails user,
            @Valid @RequestBody CommentCreateRequestDto requestDto
            ){
        String accountName = user.getUsername();
        CommentResponseDto responseDto = commentService.createComment(accountName, requestDto);
        return responseDto;
    }
}
