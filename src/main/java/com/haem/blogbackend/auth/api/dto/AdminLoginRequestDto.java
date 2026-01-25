package com.haem.blogbackend.auth.api.dto;

import lombok.Getter;

@Getter
public class AdminLoginRequestDto {
    private String accountName;
    private String password;

    public AdminLoginRequestDto(){}

    public AdminLoginRequestDto(String accountName, String password){
        this.accountName = accountName;
        this.password = password;
    }
}
