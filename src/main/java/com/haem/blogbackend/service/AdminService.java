package com.haem.blogbackend.service;

import com.haem.blogbackend.dto.response.TokenVerifyResponseDto;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.haem.blogbackend.config.JwtProvider;
import com.haem.blogbackend.domain.Admin;
import com.haem.blogbackend.dto.request.AdminLoginRequestDto;
import com.haem.blogbackend.dto.response.AdminLoginResponseDto;
import com.haem.blogbackend.repository.AdminRepository;

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
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 계정입니다."));

        if(!passwordEncoder.matches(tempPassword, admin.getPassword())){
            throw new IllegalArgumentException("아이디 혹은 비밀번호가 일치하지 않습니다.");
        }

        String token = jwtProvider.generateToken(admin);

        return AdminLoginResponseDto.from(admin, token);
    }

    public Admin findByAccountName(String accountName){
        return adminRepository.findByAccountName(accountName)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 계정입니다."));
    }
}
