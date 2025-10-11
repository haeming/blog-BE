package com.haem.blogbackend.dto.response;

import com.haem.blogbackend.domain.Admin;
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
