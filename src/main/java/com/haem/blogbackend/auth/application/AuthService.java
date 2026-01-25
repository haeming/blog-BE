package com.haem.blogbackend.auth.application;

import com.haem.blogbackend.auth.infrastructure.JwtProvider;
import com.haem.blogbackend.auth.domain.AdminRepository;
import com.haem.blogbackend.auth.domain.AdminNotFoundException;
import com.haem.blogbackend.auth.domain.ExpiredTokenException;
import com.haem.blogbackend.auth.domain.Admin;
import com.haem.blogbackend.auth.api.dto.AdminLoginRequestDto;
import com.haem.blogbackend.auth.api.dto.AdminLoginResponseDto;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public AuthService(AdminRepository adminRepository, PasswordEncoder passwordEncoder, JwtProvider jwtProvider) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    public AdminLoginResponseDto adminLogin(AdminLoginRequestDto requestDto){
        Admin admin = getVerifiedAdmin(requestDto);
        String token = jwtProvider.generateToken(admin);

        return AdminLoginResponseDto.from(admin, token);
    }

    public Admin verifyAndGetAdmin (String token){
        String accountName = getAccountNameIfTokenValid(token);
        return getAdminOrThrow(accountName);
    }

    private Admin getVerifiedAdmin(AdminLoginRequestDto requestDto){
        Admin admin = getAdminOrThrow(requestDto.getAccountName());
        if(!passwordEncoder.matches(requestDto.getPassword(), admin.getPassword())){
            throw new IllegalArgumentException("아이디 혹은 비밀번호가 일치하지 않습니다.");
        }
        return admin;
    }

    private Admin getAdminOrThrow(String accountName){
        return adminRepository.findByAccountName(accountName)
                .orElseThrow(() -> new AdminNotFoundException(accountName));
    }

    private String getAccountNameIfTokenValid(String token){
        if(!jwtProvider.validateToken(token)){
            throw new ExpiredTokenException("토큰이 만료되었거나 유효하지 않습니다.");
        }
        return jwtProvider.getAccountName(token);
    }
}
