package com.haem.blogbackend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.haem.blogbackend.repository.CommentRepository;

@Transactional(readOnly = true)
@Service
public class CommentService {
    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }
}
