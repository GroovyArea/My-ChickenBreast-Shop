package com.daniel.mychickenbreastshop.global.auth.security.error.handler;

import com.daniel.mychickenbreastshop.global.auth.security.error.exception.CustomAuthenticationEntrypointException;
import com.daniel.mychickenbreastshop.global.auth.security.error.exception.LoginFailedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class AuthControllerAdvice {

    @ExceptionHandler(LoginFailedException.class)
    public ResponseEntity<String> loginFailedExceptionHandle(LoginFailedException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.internalServerError().body(e.getMessage());
    }

    @ExceptionHandler(CustomAuthenticationEntrypointException.class)
    public ResponseEntity<String> authenticationEntrypointExceptionHandle(CustomAuthenticationEntrypointException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.internalServerError().body(e.getMessage());
    }
}
