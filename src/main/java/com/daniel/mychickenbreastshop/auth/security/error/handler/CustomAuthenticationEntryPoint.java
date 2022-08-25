package com.daniel.mychickenbreastshop.auth.security.error.handler;

import com.daniel.mychickenbreastshop.auth.jwt.model.JwtErrorMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String insertedExceptionMessage = (String) request.getAttribute("exception");

        String defaultExceptionMessage = authException.getMessage();

        List<String> jwtErrorMessageList = Arrays.stream(JwtErrorMessage.values())
                .map(JwtErrorMessage::getMessage).toList();

        if (!jwtErrorMessageList.contains(insertedExceptionMessage)) {
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().print(defaultExceptionMessage);
            return;
        }

        if (insertedExceptionMessage.equals(JwtErrorMessage.MALFORMED.getMessage())) {
            setResponse(response, JwtErrorMessage.MALFORMED);
        } else if (insertedExceptionMessage.equals(JwtErrorMessage.CLASS_CAST_FAIL.getMessage())) {
            setResponse(response, JwtErrorMessage.CLASS_CAST_FAIL);
        } else if (insertedExceptionMessage.equals(JwtErrorMessage.EXPIRED.getMessage())) {
            setResponse(response, JwtErrorMessage.EXPIRED);
        } else if (insertedExceptionMessage.equals(JwtErrorMessage.INVALID_SIGNATURE.getMessage())) {
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
