package com.haem.blogbackend.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT))
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Admin admin;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "updated_at", updatable = false, insertable = false)
    private LocalDateTime updatedAt;

    @Column(name = "created_at", updatable = false, insertable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    // 기본생성자
    protected Post(){}

    public Post(Category category, Admin admin, String title, String content){
        this.category = category;
        this.admin = admin;
        this.title = title;
        this.content = content;
    }

    // getter, setter
    public Long getId(){
        return id;
    }
    public Category getCategory(){
        return category;
    }
    public Admin getAdmin(){
        return admin;
    }
    public String getTitle(){
        return title;
    }
    public String getContent(){
        return content;
    }
    public List<Image> getImages(){
        return images;
    }
    public LocalDateTime getUpdatedAt(){
        return updatedAt;
    }
    public LocalDateTime getCreatedAt(){
        return createdAt;
    }

    public void setCategory(Category category){
        this.category = category;
    }
    public void setAdmin(Admin admin){
        this.admin = admin;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public void setContent(String content){
        this.content = content;
    }
    public void setImages(List<Image> images){
        this.images = images;
    }

    public void addImage(Image image) {
        images.add(image);
        image.setPost(this);
    }

    public void removeImage(Image image) {
        images.remove(image);
        image.setPost(null);
    }
}
