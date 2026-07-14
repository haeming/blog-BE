package com.haem.blogbackend.auth.application;

import com.haem.blogbackend.auth.infrastructure.JwtProvider;
import com.haem.blogbackend.auth.domain.AdminRepository;
import com.haem.blogbackend.auth.domain.AdminNotFoundException;
import com.haem.blogbackend.auth.domain.ExpiredTokenException;
import com.haem.blogbackend.auth.domain.InvalidCredentialsException;
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
    private final LoginRateLimitService loginRateLimitService;

    public AuthService(AdminRepository adminRepository, PasswordEncoder passwordEncoder, JwtProvider jwtProvider, LoginRateLimitService loginRateLimitService) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
        this.loginRateLimitService = loginRateLimitService;
    }

    public AdminLoginResponseDto adminLogin(AdminLoginRequestDto requestDto, String ipAddress){
        loginRateLimitService.checkAllowed(ipAddress);

        Admin admin;
        try {
            admin = getVerifiedAdmin(requestDto);
        } catch (InvalidCredentialsException e) {
            loginRateLimitService.recordFailure(ipAddress);
            throw e;
        }

        String token = jwtProvider.generateToken(admin);
        return AdminLoginResponseDto.from(admin, token);
    }

    public Admin verifyAndGetAdmin (String token){
        String accountName = getAccountNameIfTokenValid(token);
        return getAdminOrThrow(accountName);
    }

    private Admin getVerifiedAdmin(AdminLoginRequestDto requestDto){
        Admin admin = adminRepository.findByAccountName(requestDto.getAccountName())
                .orElseThrow(InvalidCredentialsException::new);
        if(!passwordEncoder.matches(requestDto.getPassword(), admin.getPassword())){
            throw new InvalidCredentialsException();
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
