package com.haem.blogbackend.exception.notfound;

import com.haem.blogbackend.exception.base.NotFoundException;

public class AdminNotFoundException extends NotFoundException{
    public AdminNotFoundException(String accountName) {
        super("존재하지 않는 관리자입니다. accountName=" + accountName);
    }

}
