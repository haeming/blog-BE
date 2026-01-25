package com.haem.blogbackend.auth.domain;

import com.haem.blogbackend.global.error.exception.base.NotFoundException;

public class AdminNotFoundException extends NotFoundException {
    public AdminNotFoundException(String accountName) {
        super(String.format("존재하지 않는 관리자입니다. accountName = %s", accountName));
    }

}
