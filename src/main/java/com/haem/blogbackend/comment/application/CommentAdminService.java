package com.haem.blogbackend.comment.application;

import com.haem.blogbackend.comment.domain.*;
import com.haem.blogbackend.global.util.EntityFinder;
import com.haem.blogbackend.comment.application.dto.CommentResult;
import com.haem.blogbackend.auth.domain.AdminRepository;
import com.haem.blogbackend.post.domain.PostRepository;
import com.haem.blogbackend.auth.domain.AdminNotFoundException;
import com.haem.blogbackend.post.domain.PostNotFoundException;
import com.haem.blogbackend.auth.domain.Admin;
import com.haem.blogbackend.post.domain.Post;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Transactional(readOnly = true)
@Service
public class CommentAdminService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final AdminRepository adminRepository;
    private final EntityFinder entityFinder;

    public CommentAdminService(
            CommentRepository commentRepository,
            PostRepository postRepository,
            AdminRepository adminRepository,
            EntityFinder entityFinder) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.adminRepository = adminRepository;
        this.entityFinder = entityFinder;
    }

    public long getCommentCount (){
        return commentRepository.countByDeletedAtIsNull();
    }

    public List<CommentResult> getCommentsByPostId(Long postId){
        List<Comment> comments = commentRepository.findByPostIdAndDeletedAtIsNull(postId);
        return comments.stream()
                .map(CommentResult::from)
                .toList();
    }

    @Transactional
    public CommentResult createComment(String accountName, CommentCreateCommand command){
        Post post = getPostOrThrow(command.postId());
        Admin admin = getAdminOrThrow(accountName);
        Comment parent = findParentCommentOrNull(command.parentId());

        validateParentBelongsToPost(parent, command.postId());

        Comment comment = Comment.createByAdmin(post, admin, parent, command.content());
        commentRepository.save(comment);
        return CommentResult.from(comment);
    }

    @Transactional
    public CommentResult updateComment(Long id, String accountName, CommentUpdateCommand command) {
        Comment comment = getCommentOrThrow(id);
        Admin admin = getAdminOrThrow(accountName);

        // 작성자 검증
        if (!comment.getAdmin().getId().equals(admin.getId())) {
            throw new IllegalArgumentException("본인의 댓글만 수정할 수 있습니다.");
        }

        comment.setContent(command.content());
        return CommentResult.from(comment);
    }

    @Transactional
    public void deleteComment(Long id){
        Comment comment = getCommentOrThrow(id);
        comment.softDelete();
    }

    private Comment getCommentOrThrow(Long commentId){
        return entityFinder.findByIdOrThrow(
                commentId,
                commentRepository,
                () -> new CommentNotFoundException(commentId)
        );
    }

    private Post getPostOrThrow(Long postId) {
        return entityFinder.findByIdOrThrow(
                postId,
                postRepository,
                () -> new PostNotFoundException(postId)
        );
    }

    private Admin getAdminOrThrow(String accountName) {
        return entityFinder.findByStringKeyOrThrow(
                accountName,
                adminRepository::findByAccountName,
                () -> new AdminNotFoundException(accountName)
        );
    }

    private Comment findParentCommentOrNull (Long parentId) {
        if(parentId == null){
            return null;
        }
        return entityFinder.findByIdOrThrow(
                parentId,
                commentRepository,
                () -> new CommentNotFoundException(parentId)
        );
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
}