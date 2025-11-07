package com.haem.blogbackend.admin.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.haem.blogbackend.admin.component.JwtProvider;
import com.haem.blogbackend.domain.Admin;
import com.haem.blogbackend.dto.request.AdminLoginRequestDto;
import com.haem.blogbackend.dto.response.AdminLoginResponseDto;
import com.haem.blogbackend.dto.response.TokenVerifyResponseDto;
import com.haem.blogbackend.common.exception.token.ExpiredTokenException;
import com.haem.blogbackend.common.exception.token.InvalidTokenException;
import com.haem.blogbackend.admin.service.AdminService;

@RestController
@RequestMapping("/api/admin")
public class AuthController {
    private final AdminService adminService;
    private final JwtProvider jwtProvider;

    public AuthController(AdminService adminService, JwtProvider jwtProvider) {
        this.adminService = adminService;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping("/login")
    public AdminLoginResponseDto adminLogin(@RequestBody AdminLoginRequestDto requestDto){
        return adminService.adminLogin(requestDto);
    }

    @GetMapping("/verify-token")
    public TokenVerifyResponseDto verifyToken(@RequestHeader(value = "Authorization", required = false) String authHeader){
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            throw new InvalidTokenException();
        }
        String token = authHeader.substring(7);

        if(!jwtProvider.validateToken(token)){
            throw new ExpiredTokenException();
        }

        String accountName = jwtProvider.getAccountName(token);
        Admin admin = adminService.findByAccountName(accountName);
        TokenVerifyResponseDto response = TokenVerifyResponseDto.from(admin);

        return response;
    }

}
