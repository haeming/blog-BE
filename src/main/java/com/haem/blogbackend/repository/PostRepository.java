package com.haem.blogbackend.repository;

import com.haem.blogbackend.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByTitleContaining(String title, Pageable pageable);
    Page<Post> findPostByCategoryContaining(String category, Pageable pageable);
}
