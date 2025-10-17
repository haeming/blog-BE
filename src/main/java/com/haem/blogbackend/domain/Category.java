package com.haem.blogbackend.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

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

    @OneToMany(mappedBy = "category")
    private List<Post> posts = new ArrayList<>();

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

    public static Category create(String categoryName){
        return new Category(categoryName);
    }

    public void updateName(String name) {
        this.categoryName = name;
    }

    public void updateImage(String imageUrl, String originalName) {
        this.imageUrl = imageUrl;
        this.originalName = originalName;
    }


    public static Category create(String categoryName, String imageUrl, String originalName){
        return new Category(categoryName, imageUrl, originalName);
    }
    
}
