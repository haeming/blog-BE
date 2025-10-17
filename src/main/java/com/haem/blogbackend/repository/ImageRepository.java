package com.haem.blogbackend.repository;

import com.haem.blogbackend.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Integer> {
}
