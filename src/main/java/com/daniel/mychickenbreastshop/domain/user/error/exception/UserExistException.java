package com.daniel.mychickenbreastshop.domain.user.error.exception;

import com.daniel.mychickenbreastshop.domain.user.domain.UserResponse;

public class UserExistException extends RuntimeException {
    public UserExistException() {
        super(UserResponse.USER_EXISTS_MESSAGE.getMessage());
    }
}
