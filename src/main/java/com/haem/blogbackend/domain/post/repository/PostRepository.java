package com.haem.blogbackend.domain.post.repository;

import com.haem.blogbackend.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByDeletedAtIsNull(Pageable pageable);

    long countByDeletedAtIsNull();

    long countByCategoryId(Long categoryId);
}
