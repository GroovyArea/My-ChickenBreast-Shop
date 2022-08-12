package com.daniel.mychickenbreastshop.global.error.handler;

import com.daniel.mychickenbreastshop.global.error.exception.AuthorizationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 전역 컨트롤러 어드바이스 <br>
 * Runtime Exception handling
 *
 * <pre>
 *     <b>history</b>
 *     1.0, 2022.08.03 최초 작성
 * </pre>
 *
 * @author 김남영
 * @version 1.0
 */
@RestControllerAdvice
@Slf4j
public class GlobalAdvice {

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<String> authorizationExceptionHandle(AuthorizationException e) {
        return ResponseEntity.badRequest().body("Exception message : " + e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> illegalArgumentExceptionHandle(IllegalArgumentException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.badRequest().body("Exception message : " + e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> runtimeExceptionHandle(RuntimeException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.badRequest().body("Exception message : " + e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> exceptionHandle(Exception e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.badRequest().body("Exception message : " + e.getMessage());
    }

}
