package com.daniel.mychickenbreastshop.global.error.exception;

public class TokenMismatchException extends Exception {

    public TokenMismatchException(String message) {
        super(message);
    }

    public TokenMismatchException(String message, Throwable cause) {
        super(message, cause);
    }
}
