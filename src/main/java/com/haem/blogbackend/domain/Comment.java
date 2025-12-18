package com.haem.blogbackend.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin admin;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @Column(name = "nickname", nullable = true, length = 100)
    private String nickname;

    @Column(name = "password", nullable = true, length = 100)
    private String password;

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "is_pinned", nullable = false)
    private boolean isPinned;

    @Column(name = "created_at", updatable = false, insertable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false)
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    // 기본생성자
    protected Comment(){}

    public Comment(Post post, Admin admin, Comment parent, String nickname, String password, String content, Boolean isPinned){
        this.post = post;
        this.admin = admin;
        this.parent = parent;
        this.nickname = nickname;
        this.password = password;
        this.content = content;
        this.isPinned = isPinned;
    }

    // getter, setter

    public Long getId(){
        return id;
    }

    public Post getPost(){
        return post;
    }

    public Admin getAdmin(){
        return admin;
    }

    public Comment getParent(){
        return parent;
    }

    public String getNickname(){
        return nickname;
    }

    public String getPassword(){
        return password;
    }

    public String getContent(){
        return content;
    }

    public Boolean getIsPinned(){
        return isPinned;
    }

    public LocalDateTime getCreatedAt(){
        return createdAt;
    }

    public LocalDateTime getUpdatedAt(){
        return updatedAt;
    }

    public void setPost(Post post){
        this.post = post;
    }

    public void setAdmin(Admin admin){
        this.admin = admin;
    }

    public void setParent(Comment parent){
        this.parent = parent;
    }

    public void setNickname(String nickname){
        this.nickname = nickname;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public static Comment createByAdmin(Post post, Admin admin, Comment parent, String content) {
        return new Comment(post, admin, parent, null, null, content, false);
    }

    public static Comment createByUser(Post post, Comment parent, String nickname, String password, String content) {
        return new Comment(post, null, parent, nickname, password, content, false);
    }

    public void softDelete(){
        deletedAt = LocalDateTime.now();
    }
}
