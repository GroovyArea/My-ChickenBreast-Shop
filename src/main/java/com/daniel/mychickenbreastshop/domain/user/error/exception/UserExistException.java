package com.daniel.mychickenbreastshop.domain.user.error.exception;

import com.daniel.mychickenbreastshop.domain.user.enums.ResponseMessages;

public class UserExistException extends RuntimeException {
    public UserExistException() {
        super(ResponseMessages.USER_EXISTS_MESSAGE.getMessage());
    }
}
