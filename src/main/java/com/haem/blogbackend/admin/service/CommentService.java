package com.haem.blogbackend.admin.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.haem.blogbackend.admin.repository.AdminRepository;
import com.haem.blogbackend.admin.repository.CommentRepository;
import com.haem.blogbackend.admin.repository.PostRepository;
import com.haem.blogbackend.common.exception.notfound.AdminNotFoundException;
import com.haem.blogbackend.common.exception.notfound.PostNotFoundException;
import com.haem.blogbackend.domain.Admin;
import com.haem.blogbackend.domain.Comment;
import com.haem.blogbackend.domain.Post;
import com.haem.blogbackend.dto.request.CommentCreateRequestDto;
import com.haem.blogbackend.dto.response.CommentResponseDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional(readOnly = true)
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final AdminRepository adminRepository;

    public CommentService(
            CommentRepository commentRepository,
            PostRepository postRepository,
            AdminRepository adminRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.adminRepository = adminRepository;
    }

    @Transactional
    public CommentResponseDto createComment(String accountName, CommentCreateRequestDto requestDto){
        Post post = postRepository.findById(requestDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException(requestDto.getPostId()));

        Admin admin = adminRepository.findByAccountName(accountName)
                .orElseThrow(() -> new AdminNotFoundException(accountName));

        Comment parent = null;
        if (requestDto.getParentId() != null) {
            parent = commentRepository.findById(requestDto.getParentId())
                    .orElseThrow(() -> new RuntimeException("해당 댓글을 찾을 수 없습니다."));
        }

        Comment comment = Comment.createByAdmin(post, admin, parent, requestDto.getContent());
        commentRepository.save(comment);
        return CommentResponseDto.from(comment);
    }
}
