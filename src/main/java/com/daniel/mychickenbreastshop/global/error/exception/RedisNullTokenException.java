package com.daniel.mychickenbreastshop.global.error.exception;

public class RedisNullTokenException extends Exception{

    public RedisNullTokenException(String message) {
        super(message);
    }

    public RedisNullTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
