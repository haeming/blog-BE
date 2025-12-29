package com.haem.blogbackend.domain.admin.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.haem.blogbackend.domain.admin.entity.Admin;


public interface AdminRepository extends JpaRepository<Admin, Long>{
    Optional<Admin> findByAccountName(String accountName);
}
