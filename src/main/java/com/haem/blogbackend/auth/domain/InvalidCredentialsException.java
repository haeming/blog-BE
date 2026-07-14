package com.haem.blogbackend.auth.domain;

import com.haem.blogbackend.global.error.exception.base.TokenException;

public class InvalidCredentialsException extends TokenException {
    public InvalidCredentialsException() {
        super("아이디 또는 비밀번호가 올바르지 않습니다.");
    }
}
