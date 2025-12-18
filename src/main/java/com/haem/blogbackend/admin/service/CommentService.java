package com.haem.blogbackend.admin.service;

import com.haem.blogbackend.admin.component.EntityFinder;
import com.haem.blogbackend.admin.dto.response.CommentResponseDto;
import com.haem.blogbackend.admin.repository.AdminRepository;
import com.haem.blogbackend.admin.repository.CommentRepository;
import com.haem.blogbackend.admin.repository.PostRepository;
import com.haem.blogbackend.admin.service.dto.CommentCreateCommand;
import com.haem.blogbackend.common.exception.notfound.AdminNotFoundException;
import com.haem.blogbackend.common.exception.notfound.CommentNotFoundException;
import com.haem.blogbackend.common.exception.notfound.PostNotFoundException;
import com.haem.blogbackend.domain.Admin;
import com.haem.blogbackend.domain.Comment;
import com.haem.blogbackend.domain.Post;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Transactional(readOnly = true)
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final AdminRepository adminRepository;
    private final EntityFinder entityFinder;

    public CommentService(
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

    public List<CommentResponseDto> getCommentsByPostId(Long postId){
        List<Comment> comments = commentRepository.findByPostIdAndDeletedAtIsNull(postId);
        return comments.stream()
                .map(CommentResponseDto::from)
                .toList();
    }

    @Transactional
    public CommentResponseDto createComment(String accountName, CommentCreateCommand command){
        Post post = getPostOrThrow(command.postId());
        Admin admin = getAdminOrThrow(accountName);
        Comment parent = findParentCommentOrNull(command.parentId());

        validateParentBelongsToPost(parent, command.postId());

        Comment comment = Comment.createByAdmin(post, admin, parent, command.content());
        commentRepository.save(comment);
        return CommentResponseDto.from(comment);
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
                adminRepository::findByAccountName, // Function<String, Optional<Admin>> 형태로 전달
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
