package com.haem.blogbackend.admin.controller;

import com.haem.blogbackend.admin.service.AdminService;
import com.haem.blogbackend.common.exception.token.InvalidTokenException;
import com.haem.blogbackend.domain.Admin;
import com.haem.blogbackend.admin.dto.request.AdminLoginRequestDto;
import com.haem.blogbackend.admin.dto.response.AdminLoginResponseDto;
import com.haem.blogbackend.admin.dto.response.TokenVerifyResponseDto;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AuthController {
    private final AdminService adminService;

    public AuthController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/login")
    public AdminLoginResponseDto adminLogin(@RequestBody AdminLoginRequestDto requestDto){
        return adminService.adminLogin(requestDto);
    }

    @GetMapping("/check-token")
    public TokenVerifyResponseDto verifyToken(@RequestHeader(value = "Authorization", required = false) String authHeader){
        String token = extractToken(authHeader);
        Admin admin = adminService.verifyAndGetAdmin(token);

        return TokenVerifyResponseDto.from(admin);
    }

    private String extractToken(String authHeader){
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            throw new InvalidTokenException();
        }
        return authHeader.substring(7);
    }
}
