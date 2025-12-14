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
import com.haem.blogbackend.admin.dto.request.AdminCommentCreateRequestDto;
import com.haem.blogbackend.admin.dto.response.CommentResponseDto;
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
        return commentRepository.count();
    }

    public List<CommentResponseDto> getCommentsByPostId(Long postId){
        List<Comment> comments = commentRepository.findByPostId(postId);
        return comments.stream()
                .map(CommentResponseDto::from)
                .toList();
    }

    @Transactional
    public CommentResponseDto createComment(String accountName, AdminCommentCreateRequestDto requestDto){
        Post post = getPostOrThrow(requestDto.getPostId());
        Admin admin = getAdminOrThrow(accountName);
        Comment parent = findParentCommentOrNull(requestDto.getParentId());
        Comment comment = Comment.createByAdmin(post, admin, parent, requestDto.getContent());
        commentRepository.save(comment);
        return CommentResponseDto.from(comment);
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
}
