package com.haem.blogbackend.post.domain;

import com.haem.blogbackend.auth.domain.Admin;
import com.haem.blogbackend.category.domain.Category;
import com.haem.blogbackend.image.domain.Image;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT), nullable = true)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Admin admin;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_at", updatable = false, insertable = false)
    private LocalDateTime createdAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

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

    public void updateIfPresent(String title, String content) {
        boolean changed = false;

        if (title != null && !title.isBlank() && !title.equals(this.title)) {
            this.title = title;
            changed = true;
        }

        if (content != null && !content.isBlank() && !content.equals(this.content)) {
            this.content = content;
            changed = true;
        }

        if (changed) {
            touchUpdate();
        }
    }

    public void updateInfoStrict(String title, String content) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("제목 입력은 필수입니다.");
        }
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("내용을 입력해 주세요.");
        }

        this.title = title;
        this.content = content;
        touchUpdate();
    }


    public void touchUpdate(){
        this.updatedAt = LocalDateTime.now();
    }

    public void softDelete(){
        deletedAt = LocalDateTime.now();
    }

    public void addImage(Image image) {
        images.add(image);
        image.setPost(this);
    }

    public void removeImage(Image image) {
        images.remove(image);
        image.setPost(null);
    }

    public static Post create(Category category, Admin admin, String title, String content){
        if(title == null || title.isBlank()){
            throw new IllegalArgumentException("제목 입력은 필수입니다.");
        }
        if(content == null || content.isBlank()){
            throw new IllegalArgumentException("내용을 입력해 주세요.");
        }
        return new Post(category, admin, title, content);
    }

    public void updateInfo(String title, String content){
        this.title = title;
        this.content = content;
    }
}
