package com.haem.blogbackend.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.haem.blogbackend.domain.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByCategoryName(String categoryName);

}
