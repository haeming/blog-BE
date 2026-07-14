package com.haem.blogbackend.auth.domain;

import com.haem.blogbackend.global.error.exception.base.UnauthorizedException;

public class InvalidCredentialsException extends UnauthorizedException {
    public InvalidCredentialsException() {
        super("아이디 또는 비밀번호가 올바르지 않습니다.");
    }
}
