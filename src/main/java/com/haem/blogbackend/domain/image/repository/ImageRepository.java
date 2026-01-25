package com.haem.blogbackend.domain.image.repository;

import com.haem.blogbackend.domain.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Integer> {
}
