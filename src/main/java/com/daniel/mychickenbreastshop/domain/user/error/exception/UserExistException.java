package com.daniel.mychickenbreastshop.domain.user.error.exception;

import com.daniel.mychickenbreastshop.domain.user.enums.ResponseMessage;

public class UserExistException extends RuntimeException {
    public UserExistException() {
        super(ResponseMessage.USER_EXISTS_MESSAGE.getMessage());
    }
}
