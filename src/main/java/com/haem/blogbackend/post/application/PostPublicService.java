package com.haem.blogbackend.post.application;

import com.haem.blogbackend.category.domain.Category;
import com.haem.blogbackend.global.util.EntityFinder;
import com.haem.blogbackend.post.application.dto.AdjacentPostResult;
import com.haem.blogbackend.post.application.dto.PostAdjacentResult;
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

    public PostPublicService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Page<PostSummaryResult> getPublicPosts(String keyword, Pageable pageable){
        Page<Post> posts = (keyword == null || keyword.isBlank())
                ? postRepository.findByDeletedAtIsNull(pageable)
                : postRepository.searchByKeyword(escapeLikeKeyword(keyword.trim()), pageable);

        return posts.map(post -> {
            String categoryName = Optional.ofNullable(post.getCategory())
                    .map(Category::getCategoryName)
                    .orElse("미분류");
            return PostSummaryResult.from(post, categoryName);
        });
    }

    // LIKE 절의 와일드카드(%, _)를 리터럴로 취급하기 위한 이스케이프
    private static String escapeLikeKeyword(String keyword) {
        return keyword
                .replace("\\", "\\\\")
                .replace("%", "\\%")
                .replace("_", "\\_");
    }

    public PostDetailResult getPublicPost(Long id) {
        Post post = postRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new PostNotFoundException(id));
        return PostDetailResult.from(post);
    }

    public PostAdjacentResult getAdjacentPosts(Long id) {
        Post post = postRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new PostNotFoundException(id));

        AdjacentPostResult prev = postRepository
                .findFirstByCreatedAtBeforeAndDeletedAtIsNullOrderByCreatedAtDesc(post.getCreatedAt())
                .map(AdjacentPostResult::from)
                .orElse(null);

        AdjacentPostResult next = postRepository
                .findFirstByCreatedAtAfterAndDeletedAtIsNullOrderByCreatedAtAsc(post.getCreatedAt())
                .map(AdjacentPostResult::from)
                .orElse(null);

        return new PostAdjacentResult(prev, next);
    }
}
