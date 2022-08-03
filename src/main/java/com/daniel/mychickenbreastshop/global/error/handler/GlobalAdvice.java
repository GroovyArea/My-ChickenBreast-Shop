package com.daniel.mychickenbreastshop.global.error.handler;

import com.daniel.mychickenbreastshop.global.error.exception.AuthorizationException;
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
public class GlobalAdvice {

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<String> authorizationExceptionHandle(AuthorizationException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
