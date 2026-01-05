package com.haem.blogbackend.domain.category.repository;


import com.haem.blogbackend.admin.dto.response.CategoryPostCountResponse;
import com.haem.blogbackend.domain.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("""
    select new com.haem.blogbackend.admin.dto.response.CategoryPostCountResponse(
        c.id,
        c.categoryName,
        c.imageUrl,
        c.originalName,
        count(p.id)
    )
    from Category c
    left join Post p on p.category = c
    group by c.id, c.categoryName, c.imageUrl, c.originalName
    order by c.id asc
""")
    List<CategoryPostCountResponse> findCategoryPostCountsNative();

    Optional<Category> findByCategoryName(String categoryName);

}
