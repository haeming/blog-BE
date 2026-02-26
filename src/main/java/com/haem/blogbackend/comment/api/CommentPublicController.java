package com.haem.blogbackend.comment.api;

import com.haem.blogbackend.comment.api.dto.CommentSummaryResponseDto;
import com.haem.blogbackend.comment.application.CommentPublicService;
import com.haem.blogbackend.comment.application.dto.CommentSummaryResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentPublicController {
    private final CommentPublicService commentPublicService;

    public CommentPublicController(CommentPublicService commentPublicService) {
        this.commentPublicService = commentPublicService;
    }

    @GetMapping
    public List<CommentSummaryResponseDto> getComments(Long postId) {
        List<CommentSummaryResult> results = commentPublicService.getCommentsByPostId(postId);
        return results.stream()
                .map(CommentSummaryResponseDto::from)
                .toList();
    }
}
