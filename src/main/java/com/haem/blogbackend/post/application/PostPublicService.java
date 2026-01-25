package com.haem.blogbackend.post.application;

import com.haem.blogbackend.global.util.EntityFinder;
import com.haem.blogbackend.post.application.dto.PostDetailResult;
import com.haem.blogbackend.post.domain.Post;
import com.haem.blogbackend.post.domain.PostNotFoundException;
import com.haem.blogbackend.post.domain.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional(readOnly = true)
@Service
public class PostPublicService {
    private final PostRepository postRepository;

    public PostPublicService(PostRepository postRepository, EntityFinder entityFinder) {
        this.postRepository = postRepository;
    }

    public PostDetailResult getPublicPost(Long id) {
        Post post = postRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new PostNotFoundException(id));
        return PostDetailResult.from(post);
    }
}
