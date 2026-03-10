package com.haem.blogbackend.comment.application;

import com.haem.blogbackend.comment.application.dto.CommentPublicCreateCommand;
import com.haem.blogbackend.comment.application.dto.CommentResult;
import com.haem.blogbackend.comment.application.dto.CommentSummaryResult;
import com.haem.blogbackend.comment.application.dto.CommentPublicUpdateCommand;
import com.haem.blogbackend.comment.domain.Comment;
import com.haem.blogbackend.comment.domain.CommentNotFoundException;
import com.haem.blogbackend.comment.domain.CommentPasswordMismatchException;
import com.haem.blogbackend.comment.domain.CommentRepository;
import com.haem.blogbackend.global.util.EntityFinder;
import com.haem.blogbackend.post.domain.Post;
import com.haem.blogbackend.post.domain.PostNotFoundException;
import com.haem.blogbackend.post.domain.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Transactional(readOnly = true)
@Service
public class CommentPublicService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final EntityFinder entityFinder;

    public CommentPublicService(
            CommentRepository commentRepository,
            PostRepository postRepository,
            EntityFinder entityFinder
    ) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.entityFinder = entityFinder;
    }

    public List<CommentSummaryResult> getCommentsByPostId(Long postId) {
        return commentRepository.findByPostIdAndDeletedAtIsNull(postId).stream()
                .map(CommentSummaryResult::from)
                .toList();
    }

    @Transactional
    public CommentResult createComment(CommentPublicCreateCommand command) {
        Post post = getPostOrThrow(command.postId());
        Comment parent = resolveParent(command.parentId());

        Comment saved = commentRepository.save(
                Comment.createByGuest(post, parent, command.nickname(), command.password(), command.content())
        );
        return CommentResult.from(saved);
    }

    // 비밀번호 검증
    public void verifyPassword(Long commentId, String password) {
        Comment comment = getCommentOrThrow(commentId);
        validatePassword(comment, password);
    }

    @Transactional
    public CommentResult updateComment(Long commentId, CommentPublicUpdateCommand command) {
        Comment comment = getCommentOrThrow(commentId);
        validatePassword(comment, command.password());
        comment.setContent(command.content());
        return CommentResult.from(comment);
    }

    @Transactional
    public void deleteComment(Long commentId, String password) {
        Comment comment = getCommentOrThrow(commentId);
        validatePassword(comment, password);
        comment.softDelete();
    }

    private Post getPostOrThrow(Long postId) {
        return entityFinder.findByIdOrThrow(
                postId,
                postRepository,
                () -> new PostNotFoundException(postId)
        );
    }

    private Comment getCommentOrThrow(Long commentId) {
        return commentRepository.findByIdAndDeletedAtIsNull(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));
    }

    private Comment resolveParent(Long parentId) {
        if (parentId == null) return null;
        return commentRepository.findByIdAndDeletedAtIsNull(parentId)
                .orElseThrow(() -> new CommentNotFoundException(parentId));
    }

    private void validatePassword(Comment comment, String password) {
        if (!password.equals(comment.getPassword())) {
            throw new CommentPasswordMismatchException();
        }
    }
}