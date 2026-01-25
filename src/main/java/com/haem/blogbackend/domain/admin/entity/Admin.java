package com.haem.blogbackend.domain.admin.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "admin")
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_name", nullable = false, unique = true, length = 50)
    private String accountName;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(name = "created_at", updatable = false, insertable = false)
    private LocalDateTime createdAt;


    // 기본생성자
    protected Admin(){}

    public Admin(String accountName, String password){
        this.accountName = accountName;
        this.password = password;
    }

    // getter, setter
    public Long getId(){
        return id;
    }

    public String getAccountName(){
        return accountName;
    }

    public String getPassword(){
        return password;
    }

    public LocalDateTime getCreatedAt(){
        return createdAt;
    }

    public void setAccountName(String accountName){
        this.accountName = accountName;
    }

    public void setPassword(String password){
        this.password = password;
    }

}
