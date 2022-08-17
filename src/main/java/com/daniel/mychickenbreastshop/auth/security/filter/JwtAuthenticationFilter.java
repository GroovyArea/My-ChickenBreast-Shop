package com.daniel.mychickenbreastshop.auth.security.filter;

import com.daniel.mychickenbreastshop.auth.jwt.JwtAuthenticator;
import com.daniel.mychickenbreastshop.auth.jwt.JwtProvider;
import com.daniel.mychickenbreastshop.auth.jwt.JwtValidator;
import com.daniel.mychickenbreastshop.auth.security.model.PrincipalDetails;
import com.daniel.mychickenbreastshop.domain.user.dto.request.LoginRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
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
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public static final String AUTH_TYPE = "Bearer ";
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtTokenProvider;
    private final JwtValidator jwtValidator;
    private final JwtAuthenticator jwtAuthenticator;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        LoginRequestDto loginRequestDto;
        try {
            loginRequestDto = new ObjectMapper()
                    .readValue(request.getInputStream(), LoginRequestDto.class);


        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginRequestDto.getLoginId(), loginRequestDto.getPassword(), new ArrayList<>()
        );

        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        String token = jwtTokenProvider.createToken(String.valueOf(principalDetails.getId()), principalDetails.getRole());

        response.addHeader(JwtProvider.TOKEN_HEADER_KEY, AUTH_TYPE + token);
    }
}
