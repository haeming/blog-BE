package com.haem.blogbackend.admin.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.haem.blogbackend.domain.Admin;


public interface AdminRepository extends JpaRepository<Admin, Long>{
    Optional<Admin> findByAccountName(String accountName);
}
