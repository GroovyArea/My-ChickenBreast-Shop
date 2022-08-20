package com.daniel.mychickenbreastshop.auth.security.error.handler;

import com.daniel.mychickenbreastshop.auth.jwt.model.JwtErrorMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String exceptionMessage = (String) request.getAttribute("exception");

        if (exceptionMessage.equals(JwtErrorMessage.MALFORMED.getMessage())) {
            setResponse(response, JwtErrorMessage.MALFORMED);
        } else if (exceptionMessage.equals(JwtErrorMessage.CLASS_CAST_FAIL.getMessage())) {
            setResponse(response, JwtErrorMessage.CLASS_CAST_FAIL);
        } else if (exceptionMessage.equals(JwtErrorMessage.EXPIRED.getMessage())) {
            setResponse(response, JwtErrorMessage.EXPIRED);
        } else if (exceptionMessage.equals(JwtErrorMessage.INVALID_SIGNATURE.getMessage())) {
            setResponse(response, JwtErrorMessage.INVALID_SIGNATURE);
        } else {
            setResponse(response, JwtErrorMessage.UNSUPPORTED);
        }

    }

    private void setResponse(HttpServletResponse response, JwtErrorMessage jwtErrorMessage) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().print(jwtErrorMessage.getMessage());
    }
}
