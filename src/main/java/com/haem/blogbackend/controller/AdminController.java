package com.haem.blogbackend.controller;

import com.haem.blogbackend.config.JwtProvider;
import com.haem.blogbackend.domain.Admin;
import com.haem.blogbackend.dto.response.ApiResponse;
import com.haem.blogbackend.dto.response.TokenVerifyResponseDto;
import com.haem.blogbackend.exception.ExpiredTokenException;
import com.haem.blogbackend.exception.GlobalExceptionHandler;
import com.haem.blogbackend.exception.InvalidTokenException;
import com.haem.blogbackend.exception.TokenException;
import org.springframework.web.bind.annotation.*;

import com.haem.blogbackend.dto.request.AdminLoginRequestDto;
import com.haem.blogbackend.dto.response.AdminLoginResponseDto;
import com.haem.blogbackend.service.AdminService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final AdminService adminService;
    private final JwtProvider jwtProvider;

    public AdminController(AdminService adminService, JwtProvider jwtProvider) {
        this.adminService = adminService;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping("/login")
    public AdminLoginResponseDto adminLogin(@RequestBody AdminLoginRequestDto requestDto){
        return adminService.adminLogin(requestDto);
    }

    @GetMapping("/verify-token")
    public ApiResponse<TokenVerifyResponseDto> verifyToken(@RequestHeader(value = "Authorization", required = false) String authHeader){
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

        return ApiResponse.ok(response);
    }

}
