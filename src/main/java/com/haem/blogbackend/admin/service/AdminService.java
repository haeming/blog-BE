package com.haem.blogbackend.admin.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.haem.blogbackend.admin.component.JwtProvider;
import com.haem.blogbackend.domain.Admin;
import com.haem.blogbackend.dto.request.AdminLoginRequestDto;
import com.haem.blogbackend.dto.response.AdminLoginResponseDto;
import com.haem.blogbackend.common.exception.notfound.AdminNotFoundException;
import com.haem.blogbackend.admin.repository.AdminRepository;

@Service
public class AdminService {
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public AdminService(AdminRepository adminRepository, PasswordEncoder passwordEncoder, JwtProvider jwtProvider) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    public AdminLoginResponseDto adminLogin(AdminLoginRequestDto requestDto){
        String accountName = requestDto.getAccountName();
        String tempPassword = requestDto.getPassword();

        Admin admin = adminRepository.findByAccountName(accountName)
            .orElseThrow(() -> new AdminNotFoundException(accountName));

        if(!passwordEncoder.matches(tempPassword, admin.getPassword())){
            throw new IllegalArgumentException("아이디 혹은 비밀번호가 일치하지 않습니다.");
        }

        String token = jwtProvider.generateToken(admin);

        return AdminLoginResponseDto.from(admin, token);
    }

    public Admin findByAccountName(String accountName){
        return adminRepository.findByAccountName(accountName)
                .orElseThrow(() -> new AdminNotFoundException(accountName));
    }
}
