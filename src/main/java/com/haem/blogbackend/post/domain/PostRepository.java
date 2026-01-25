package com.haem.blogbackend.post.domain;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    // public / 기본 조회(삭제 제외)
    Page<Post> findByDeletedAtIsNull(Pageable pageable);
    Optional<Post> findByIdAndDeletedAtIsNull(Long id);

    long countByDeletedAtIsNull();
    Page<Post> findByCategoryIdAndDeletedAtIsNull(Long categoryId, Pageable pageable);
    long countByCategoryIdAndDeletedAtIsNull(Long categoryId);

    // admin / 삭제된 글 조회 관리 (삭제 포함 or 삭제만)
    Page<Post> findByDeletedAtIsNotNull(Pageable pageable);     // 휴지통 목록
}
