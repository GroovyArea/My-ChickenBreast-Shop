package com.daniel.mychickenbreastshop.auth.security.filter.custom;

import com.daniel.mychickenbreastshop.auth.jwt.JwtProvider;
import com.daniel.mychickenbreastshop.auth.jwt.model.JwtProperties;
import com.daniel.mychickenbreastshop.auth.security.model.PrincipalDetails;
import com.daniel.mychickenbreastshop.auth.security.model.dto.LoginRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 토큰의 유효성 검사를 진행하는 필터
 *
 * <pre>
 *     <b>history</b>
 *     1.0, 2022.08.12 최초 작성
 * </pre>
 *
 * @author 김남영
 * @version 1.0
 */

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtProvider jwtProvider;
    private final ObjectMapper objectMapper;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager, JwtProvider jwtProvider, ObjectMapper objectMapper) {
        super(authenticationManager);
        this.jwtProvider = jwtProvider;
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try {
            LoginRequestDto loginRequestDto = objectMapper
                    .readValue(request.getInputStream(), LoginRequestDto.class);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    loginRequestDto.getLoginId(), loginRequestDto.getPassword(), new ArrayList<>()
            );

            return getAuthenticationManager().authenticate(authenticationToken);
        } catch (Exception e) {
            throw new BadCredentialsException(e.getMessage(), e);
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().println(failed.getMessage());
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        String token = jwtProvider.createToken(String.valueOf(principalDetails.getId()), principalDetails.getLoginId(), principalDetails.getRole());

        response.addHeader(JwtProperties.TOKEN_HEADER_KEY.getKey(), JwtProperties.AUTH_TYPE.getKey() + token);
    }


}
