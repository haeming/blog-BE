package com.haem.blogbackend.repository;


import java.util.List;
import java.util.Optional;

import com.haem.blogbackend.dto.response.CategoryResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;

import com.haem.blogbackend.domain.Category;
import org.springframework.data.jpa.repository.Query;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByCategoryName(String categoryName);

}
