package com.haem.blogbackend.auth.domain;

import com.haem.blogbackend.global.error.exception.base.TooManyRequestsException;

public class LoginRateLimitExceededException extends TooManyRequestsException {
    public LoginRateLimitExceededException() {
        super("로그인 시도가 너무 많습니다. 잠시 후 다시 시도해주세요.");
    }
}
