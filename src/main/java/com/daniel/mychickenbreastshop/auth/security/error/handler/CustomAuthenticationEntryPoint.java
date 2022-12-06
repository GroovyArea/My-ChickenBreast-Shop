package com.daniel.mychickenbreastshop.auth.security.error.handler;

import com.daniel.mychickenbreastshop.auth.jwt.model.JwtErrorMessage;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static com.daniel.mychickenbreastshop.auth.jwt.model.JwtErrorMessage.*;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String insertedExceptionMessage = (String) request.getAttribute("exception");

        String defaultExceptionMessage = authException.getMessage();

        List<String> jwtErrorMessages = Arrays.stream(values())
                .map(JwtErrorMessage::getMessage).toList();

        if (!jwtErrorMessages.contains(insertedExceptionMessage)) {
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().print(defaultExceptionMessage);
            return;
        }

        if (insertedExceptionMessage.equals(MALFORMED.getMessage())) {
            setResponse(response, MALFORMED);
        } else if (insertedExceptionMessage.equals(CLASS_CAST_FAIL.getMessage())) {
            setResponse(response, CLASS_CAST_FAIL);
        } else if (insertedExceptionMessage.equals(EXPIRED.getMessage())) {
            setResponse(response, EXPIRED);
        } else if (insertedExceptionMessage.equals(INVALID_SIGNATURE.getMessage())) {
            setResponse(response, INVALID_SIGNATURE);
        } else {
            setResponse(response, UNSUPPORTED);
        }

    }

    private void setResponse(HttpServletResponse response, JwtErrorMessage jwtErrorMessage) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().print(jwtErrorMessage.getMessage());
    }
}
