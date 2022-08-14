package com.daniel.mychickenbreastshop.global.auth.security.error.exception;

public class CustomAuthenticationEntrypointException extends RuntimeException {

    public CustomAuthenticationEntrypointException() {
    }

    public CustomAuthenticationEntrypointException(String message) {
        super(message);
    }

    public CustomAuthenticationEntrypointException(String message, Throwable cause) {
        super(message, cause);
    }
}
