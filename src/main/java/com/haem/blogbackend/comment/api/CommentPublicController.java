package com.haem.blogbackend.comment.api;

import com.haem.blogbackend.comment.api.dto.*;
import com.haem.blogbackend.comment.application.CommentPublicService;
import com.haem.blogbackend.comment.application.dto.CommentCreateCommand;
import com.haem.blogbackend.comment.application.dto.CommentResult;
import com.haem.blogbackend.comment.application.dto.CommentSummaryResult;
import com.haem.blogbackend.comment.application.dto.CommentUpdateCommand;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

    // POST /api/comments
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponseDto createComment(@RequestBody PublicCommentCreateRequestDto request) {
        CommentCreateCommand command = new CommentCreateCommand(
                request.getPostId(),
                request.getParentId(),
                request.getNickname(),
                request.getPassword(),
                request.getContent()
        );
        CommentResult result = commentPublicService.createComment(command);
        return CommentResponseDto.from(result);
    }

    // POST /api/comments/{id}/verify-password
    @PostMapping("/{id}/verify-password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void verifyPassword(
            @PathVariable Long id,
            @RequestBody PublicCommentVerifyPasswordRequestDto request
    ) {
       commentPublicService.verifyPassword(id, request.getPassword());
    }

    // PUT /api/comments/{id}
    @PutMapping("/{id}")
    public CommentResponseDto updateComment(
            @PathVariable Long id,
            @RequestBody PublicCommentUpdateRequestDto request
    ) {
        CommentUpdateCommand command = new CommentUpdateCommand(
                request.getPassword(),
                request.getContent()
        );
        CommentResult result = commentPublicService.updateComment(id, command);
        return CommentResponseDto.from(result);
    }

    // DELETE /api/comments/{id}
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(
            @PathVariable Long id,
            @RequestBody PublicCommentDeleteRequestDto request
    ) {
        commentPublicService.deleteComment(id, request.getPassword());
    }
}
