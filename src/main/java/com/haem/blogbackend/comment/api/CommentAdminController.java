package com.haem.blogbackend.comment.api;

import com.haem.blogbackend.comment.api.dto.*;
import com.haem.blogbackend.comment.application.CommentAdminService;
import com.haem.blogbackend.comment.application.dto.CommentResult;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/comments")
public class CommentAdminController {
    private final CommentAdminService commentAdminService;

    public CommentAdminController(CommentAdminService commentAdminService) {
        this.commentAdminService = commentAdminService;
    }

    @GetMapping
    public List<CommentResponseDto> getComments(Long postId) {
        List<CommentResult> results = commentAdminService.getCommentsByPostId(postId);
        return results.stream()
                .map(CommentResponseDto::from)
                .toList();
    }

    @GetMapping("/count")
    public long getCommentCount() {
        return commentAdminService.getCommentCount();
    }

    @PostMapping
    public CommentResponseDto createComment(
            @AuthenticationPrincipal UserDetails admin,
            @Valid @RequestBody AdminCommentCreateRequestDto requestDto
    ) {
        CommentAdminCreateCommand command = new CommentAdminCreateCommand(
                requestDto.getPostId(),
                requestDto.getParentId(),
                requestDto.getContent()
        );
        CommentResult result = commentAdminService.createComment(admin.getUsername(), command);
        return CommentResponseDto.from(result);
    }

    @DeleteMapping("/{id}")
    public void deleteComment (@PathVariable("id") Long id) {
        commentAdminService.deleteComment(id);
    }

    @PatchMapping("/{id}")
    public CommentResponseDto updateComment(
            @PathVariable("id") Long id,
            @AuthenticationPrincipal UserDetails admin,
            @Valid @RequestBody AdminCommentUpdateRequestDto requestDto
    ) {
        CommentAdminUpdateCommand command = new CommentAdminUpdateCommand(requestDto.getContent());
        CommentResult result = commentAdminService.updateComment(id, admin.getUsername(), command);
        return CommentResponseDto.from(result);
    }
}
