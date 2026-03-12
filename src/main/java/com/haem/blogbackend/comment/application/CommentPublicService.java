package com.haem.blogbackend.comment.application;

import com.haem.blogbackend.comment.application.dto.CommentPublicCreateCommand;
import com.haem.blogbackend.comment.application.dto.CommentPublicUpdateCommand;
import com.haem.blogbackend.comment.application.dto.CommentResult;
import com.haem.blogbackend.comment.application.dto.CommentSummaryResult;
import com.haem.blogbackend.comment.domain.Comment;
import com.haem.blogbackend.comment.domain.CommentNotFoundException;
import com.haem.blogbackend.comment.domain.CommentPasswordMismatchException;
import com.haem.blogbackend.comment.domain.CommentRepository;
import com.haem.blogbackend.global.util.EntityFinder;
import com.haem.blogbackend.post.domain.Post;
import com.haem.blogbackend.post.domain.PostNotFoundException;
import com.haem.blogbackend.post.domain.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;
    private final CommentContentSanitizer commentContentSanitizer;
    private final CommentValidator commentValidator;
    private final CommentRateLimitService commentRateLimitService;

    public CommentPublicService(
            CommentRepository commentRepository,
            PostRepository postRepository,
            EntityFinder entityFinder,
            PasswordEncoder passwordEncoder,
            CommentContentSanitizer commentContentSanitizer,
            CommentValidator commentValidator,
            CommentRateLimitService commentRateLimitService
    ) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.entityFinder = entityFinder;
        this.passwordEncoder = passwordEncoder;
        this.commentContentSanitizer = commentContentSanitizer;
        this.commentValidator = commentValidator;
        this.commentRateLimitService = commentRateLimitService;
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
        validateParentBelongsToPost(parent, command.postId());

        commentRateLimitService.validate(command.ipAddress());
        commentValidator.validateProfanity(command.content());
        String sanitizedContent = commentContentSanitizer.sanitize(command.content());
        String encodedPassword = passwordEncoder.encode(command.password());

        Comment saved = commentRepository.save(
                Comment.createByPublic(post, parent, command.nickname(), encodedPassword, sanitizedContent, command.ipAddress())
        );
        return CommentResult.from(saved);
    }

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

    private void validateParentBelongsToPost(Comment parent, Long postId) {
        if (parent == null) {
            return;
        }

        Long parentPostId = parent.getPost().getId();
        if (!parentPostId.equals(postId)) {
            throw new IllegalArgumentException("부모 댓글이 다른 게시글에 속해 있습니다.");
        }
    }

    private void validatePassword(Comment comment, String password) {
        if (!passwordEncoder.matches(password, comment.getPassword())) {
            throw new CommentPasswordMismatchException();
        }
    }
}
