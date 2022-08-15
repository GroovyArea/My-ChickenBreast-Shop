package com.daniel.mychickenbreastshop.auth.security.filter.handler;

import com.daniel.mychickenbreastshop.auth.security.error.exception.CustomAuthenticationEntrypointException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("/exception")
public class ExceptionController {

    @GetMapping("/entrypoint")
    public ResponseEntity<String> entryPointException() {
        throw new CustomAuthenticationEntrypointException("You don't have any permission to access that resource.");
    }

    @GetMapping("/accessDenied")
    public ResponseEntity<String> accessDeniedException() throws AccessDeniedException {
        throw new AccessDeniedException("Permission not accessible to this resource.");
    }
}
