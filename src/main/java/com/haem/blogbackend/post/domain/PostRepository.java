package com.haem.blogbackend.post.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    // public / 기본 조회(삭제 제외)
    Page<Post> findByDeletedAtIsNull(Pageable pageable);
    Optional<Post> findByIdAndDeletedAtIsNull(Long id);

    // 제목/본문 검색 (keyword는 LIKE 와일드카드가 이스케이프된 상태로 전달되어야 함)
    @Query("SELECT p FROM Post p WHERE p.deletedAt IS NULL AND " +
            "(LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) ESCAPE '\\' " +
            "OR LOWER(p.content) LIKE LOWER(CONCAT('%', :keyword, '%')) ESCAPE '\\')")
    Page<Post> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    // 이전/다음 글
    Optional<Post> findFirstByCreatedAtBeforeAndDeletedAtIsNullOrderByCreatedAtDesc(LocalDateTime createdAt);
    Optional<Post> findFirstByCreatedAtAfterAndDeletedAtIsNullOrderByCreatedAtAsc(LocalDateTime createdAt);

    long countByDeletedAtIsNull();
    Page<Post> findByCategoryIdAndDeletedAtIsNull(Long categoryId, Pageable pageable);
    long countByCategoryIdAndDeletedAtIsNull(Long categoryId);

    // admin / 삭제된 글 조회 관리 (삭제 포함 or 삭제만)
    Page<Post> findByDeletedAtIsNotNull(Pageable pageable);     // 휴지통 목록
}
