package com.haem.blogbackend.admin.dto.response;

import com.haem.blogbackend.domain.admin.entity.Admin;
import lombok.Getter;

@Getter
public class TokenVerifyResponseDto {
    private final String accountName;

    private TokenVerifyResponseDto(String accountName){
        this.accountName = accountName;
    }

    public static TokenVerifyResponseDto from(Admin admin){
        return new TokenVerifyResponseDto(admin.getAccountName());
    }
}
