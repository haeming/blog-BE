package com.haem.blogbackend.admin.dto.response;

import com.haem.blogbackend.domain.admin.entity.Admin;

import lombok.Getter;

@Getter
public class AdminLoginResponseDto {
    private final String accountName;
    private final String token;

    // private 생성자
    private AdminLoginResponseDto(String accountName, String token){
        this.accountName = accountName;
        this.token = token;
    }

    // 정적 팩토리 메서드
    public static AdminLoginResponseDto from(Admin admin, String token){
        return new AdminLoginResponseDto(admin.getAccountName(), token);
    }
}
