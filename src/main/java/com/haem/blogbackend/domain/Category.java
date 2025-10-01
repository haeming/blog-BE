package com.haem.blogbackend.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category_name", nullable = false, unique = true, length = 255)
    private String categoryName;

    @Column(name = "image_url", nullable = true, columnDefinition = "TEXT")
    private String imageUrl;

    @Column(name = "original_name", nullable = true, length = 500)
    private String originalName;

    @Column(name = "created_at", updatable = false, insertable = false)
    private LocalDateTime createdAt;

    // 기본생성자
    protected Category(){}

    public Category(String categoryName){
        this.categoryName = categoryName;
    }

    public Category(String categoryName, String imageUrl, String originalName){
        this.categoryName = categoryName;
        this.imageUrl = imageUrl;
        this.originalName = originalName;
    }

    // getter, setter
    public Long getId(){
        return id;
    }

    public String getCategoryName(){
        return categoryName;
    }

    public String getImageUrl(){
        return imageUrl;
    }

    public String getOriginalName(){
        return originalName;
    }

    public LocalDateTime getCreatedAt(){
        return createdAt;
    }

    public void setCategoryName(String categoryName){
        this.categoryName = categoryName;
    }

    public void updateImage(String newUrl, String newOriginalName) {
        this.imageUrl = newUrl;
        this.originalName = newOriginalName;
    }

    public static Category create(String categoryName){
        return new Category(categoryName);
    }

    public static Category create(String categoryName, String imageUrl, String originalName){
        return new Category(categoryName, imageUrl, originalName);
    }
    
}
