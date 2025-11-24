package com.haem.blogbackend.admin.service;

import com.haem.blogbackend.admin.component.EntityFinder;
import com.haem.blogbackend.admin.repository.AdminRepository;
import com.haem.blogbackend.admin.repository.CommentRepository;
import com.haem.blogbackend.admin.repository.PostRepository;
import com.haem.blogbackend.common.exception.notfound.AdminNotFoundException;
import com.haem.blogbackend.common.exception.notfound.CommentNotFoundException;
import com.haem.blogbackend.common.exception.notfound.PostNotFoundException;
import com.haem.blogbackend.domain.Admin;
import com.haem.blogbackend.domain.Comment;
import com.haem.blogbackend.domain.Post;
import com.haem.blogbackend.dto.request.CommentCreateRequestDto;
import com.haem.blogbackend.dto.response.CommentResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public CommentResponseDto createComment(String accountName, CommentCreateRequestDto requestDto){
        Post post = getPostOrThrow(requestDto.getPostId());
        Admin admin = getAdminOrThrow(accountName);
        Comment parent = findParentCommentOrNull(requestDto.getParentId());
        Comment comment = Comment.createByAdmin(post, admin, parent, requestDto.getContent());
        commentRepository.save(comment);
        return CommentResponseDto.from(comment);
    }

    private Post getPostOrThrow(Long postId) {
        return entityFinder.findByIdOrThrow(
                postRepository,
                postId,
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

    private Comment getCommentOrThrow(Long commentId) {
        return entityFinder.findByIdOrThrow(
                commentRepository,
                commentId,
                () -> new CommentNotFoundException(commentId)
        );
    }

    private Comment findParentCommentOrNull (Long parentId) {
        if(parentId == null){
            return null;
        }
        return entityFinder.findByIdOrThrow(
                commentRepository,
                parentId,
                () -> new CommentNotFoundException(parentId)
        );
    }
}
