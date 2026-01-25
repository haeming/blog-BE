package com.haem.blogbackend.post.application;

import com.haem.blogbackend.category.domain.Category;
import com.haem.blogbackend.global.util.EntityFinder;
import com.haem.blogbackend.post.application.dto.PostDetailResult;
import com.haem.blogbackend.post.application.dto.PostSummaryResult;
import com.haem.blogbackend.post.domain.Post;
import com.haem.blogbackend.post.domain.PostNotFoundException;
import com.haem.blogbackend.post.domain.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Transactional(readOnly = true)
@Service
public class PostPublicService {
    private final PostRepository postRepository;

    public PostPublicService(PostRepository postRepository, EntityFinder entityFinder) {
        this.postRepository = postRepository;
    }

    public Page<PostSummaryResult> getPublicPosts(Pageable pageable){
        return postRepository.findByDeletedAtIsNull(pageable)
                .map(post -> {
                    String categoryName = Optional.ofNullable(post.getCategory())
                            .map(Category::getCategoryName)
                            .orElse("미분류");
                    return PostSummaryResult.from(post, categoryName);
                });
    }

    public PostDetailResult getPublicPost(Long id) {
        Post post = postRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new PostNotFoundException(id));
        return PostDetailResult.from(post);
    }
}
