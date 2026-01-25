package com.haem.blogbackend.auth.domain;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


public interface AdminRepository extends JpaRepository<Admin, Long>{
    Optional<Admin> findByAccountName(String accountName);
}
