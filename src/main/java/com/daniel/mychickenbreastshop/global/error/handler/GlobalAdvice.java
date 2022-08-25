package com.daniel.mychickenbreastshop.global.error.handler;

import com.daniel.mychickenbreastshop.global.error.exception.BadRequestException;
import com.daniel.mychickenbreastshop.global.error.exception.InternalErrorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> methodArgumentNotValidExceptionHandle(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();

        StringBuilder builder = new StringBuilder();

        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            builder.append("[");
            builder.append(fieldError.getField());
            builder.append("](은)는 ");
            builder.append(fieldError.getDefaultMessage());
            builder.append(" 입력된 값 : [");
            builder.append(fieldError.getRejectedValue());
            builder.append("] \n");
        }
        return ResponseEntity.badRequest().body(builder.toString());
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<String> badRequestExceptionHandle(BadRequestException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.badRequest().body(setExceptionBody(e.getMessage()));
    }

    @ExceptionHandler(InternalErrorException.class)
    public ResponseEntity<String> internalErrorExceptionHandle(InternalErrorException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.internalServerError().body(setExceptionBody(e.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> runtimeExceptionHandle(RuntimeException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.internalServerError().body(setExceptionBody(e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> exceptionHandle(Exception e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.internalServerError().body(setExceptionBody(e.getMessage()));
    }

    private String setExceptionBody(String exceptionMessage) {
        return "Exception message : " + exceptionMessage;
    }
}
