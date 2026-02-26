package com.haem.blogbackend.comment.application;

import com.haem.blogbackend.comment.application.dto.CommentSummaryResult;
import com.haem.blogbackend.comment.domain.Comment;
import com.haem.blogbackend.comment.domain.CommentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Transactional(readOnly = true)
@Service
public class CommentPublicService {
    private final CommentRepository commentRepository;

    public CommentPublicService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public List<CommentSummaryResult> getCommentsByPostId(Long postId) {
        List<Comment> comments = commentRepository.findByPostIdAndDeletedAtIsNull(postId);
        return comments.stream()
                .map(CommentSummaryResult::from)
                .toList();
    }
}
