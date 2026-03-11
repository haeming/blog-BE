package com.haem.blogbackend.comment.api;

import com.haem.blogbackend.comment.api.dto.CommentResponseDto;
import com.haem.blogbackend.comment.api.dto.CommentSummaryResponseDto;
import com.haem.blogbackend.comment.api.dto.PublicCommentCreateRequestDto;
import com.haem.blogbackend.comment.api.dto.PublicCommentDeleteRequestDto;
import com.haem.blogbackend.comment.api.dto.PublicCommentUpdateRequestDto;
import com.haem.blogbackend.comment.api.dto.PublicCommentVerifyPasswordRequestDto;
import com.haem.blogbackend.comment.application.CommentPublicService;
import com.haem.blogbackend.comment.application.dto.CommentPublicCreateCommand;
import com.haem.blogbackend.comment.application.dto.CommentPublicUpdateCommand;
import com.haem.blogbackend.comment.application.dto.CommentResult;
import com.haem.blogbackend.comment.application.dto.CommentSummaryResult;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
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
    public List<CommentSummaryResponseDto> getComments(@RequestParam("postId") Long postId) {
        List<CommentSummaryResult> results = commentPublicService.getCommentsByPostId(postId);
        return results.stream()
                .map(CommentSummaryResponseDto::from)
                .toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponseDto createComment(@Valid @RequestBody PublicCommentCreateRequestDto request) {
        CommentPublicCreateCommand command = new CommentPublicCreateCommand(
                request.getPostId(),
                request.getParentId(),
                request.getNickname(),
                request.getPassword(),
                request.getContent()
        );
        CommentResult result = commentPublicService.createComment(command);
        return CommentResponseDto.from(result);
    }

    @PostMapping("/{id}/verify-password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void verifyPassword(
            @PathVariable Long id,
            @Valid @RequestBody PublicCommentVerifyPasswordRequestDto request
    ) {
        commentPublicService.verifyPassword(id, request.getPassword());
    }

    @PutMapping("/{id}")
    public CommentResponseDto updateComment(
            @PathVariable Long id,
            @Valid @RequestBody PublicCommentUpdateRequestDto request
    ) {
        CommentPublicUpdateCommand command = new CommentPublicUpdateCommand(
                request.getPassword(),
                request.getContent()
        );
        CommentResult result = commentPublicService.updateComment(id, command);
        return CommentResponseDto.from(result);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(
            @PathVariable Long id,
            @Valid @RequestBody PublicCommentDeleteRequestDto request
    ) {
        commentPublicService.deleteComment(id, request.getPassword());
    }
}
