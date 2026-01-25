package com.haem.blogbackend.category.domain;


import com.haem.blogbackend.category.infrastructure.CategoryPostCountView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("""
    select new com.haem.blogbackend.domain.category.dto.CategoryPostCountView(
        c.id,
        c.categoryName,
        count(p.id)
    )
    from Category c
    left join c.posts p
    group by c.id, c.categoryName
    order by c.id asc
""")
    List<CategoryPostCountView> findCategoryPostCounts();

    Optional<Category> findByCategoryName(String categoryName);

}
