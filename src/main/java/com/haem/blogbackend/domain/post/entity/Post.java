package com.haem.blogbackend.domain.post.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.haem.blogbackend.domain.admin.entity.Admin;
import com.haem.blogbackend.domain.category.entity.Category;
import com.haem.blogbackend.domain.image.entity.Image;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "post")
@SQLRestriction("deleted_at IS NULL")
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
