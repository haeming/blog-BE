package com.haem.blogbackend.domain.image.entity;

import java.time.LocalDateTime;

import com.haem.blogbackend.domain.post.entity.Post;
import jakarta.persistence.*;

@Entity
@Table(name = "image")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(name = "original_name", length = 255)
    private String originalName;

    @Column(name = "created_at", updatable = false, insertable = false)
    private LocalDateTime createdAt;

    // 기본생성자
    protected Image(){}

    public Image(Post post, String imageUrl, String originalName){
        this.post = post;
        this.imageUrl = imageUrl;
        this.originalName = originalName;
    }

    // getter, setter
    public Long getId(){
        return id;
    }

    public Post getPost(){
        return post;
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

    public void setPost(Post post){
        this.post = post;
    }

    public void setImageUrl(String imageUrl){
        this.imageUrl = imageUrl;
    }

    public void setOriginalName(String originalName){
        this.originalName = originalName;
    }

}
