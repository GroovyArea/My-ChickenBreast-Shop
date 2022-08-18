package com.daniel.mychickenbreastshop.auth.security.filter;

import com.daniel.mychickenbreastshop.auth.jwt.JwtProvider;
import com.daniel.mychickenbreastshop.auth.security.model.PrincipalDetails;
import com.daniel.mychickenbreastshop.domain.user.dto.request.LoginRequestDto;
import com.daniel.mychickenbreastshop.global.service.RedisService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public static final String AUTH_TYPE = "Bearer ";
    private final JwtProvider jwtTokenProvider;
    private final RedisService redisService;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtProvider jwtTokenProvider, RedisService redisService) {
        super(authenticationManager);
        this.jwtTokenProvider = jwtTokenProvider;
        this.redisService = redisService;
        setFilterProcessesUrl("/login");

    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try {
            LoginRequestDto loginRequestDto = new ObjectMapper()
                    .readValue(request.getInputStream(), LoginRequestDto.class);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    loginRequestDto.getLoginId(), loginRequestDto.getPassword(), new ArrayList<>()
            );

            return getAuthenticationManager().authenticate(authenticationToken);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        String token = jwtTokenProvider.createToken(String.valueOf(principalDetails.getId()), principalDetails.getLoginId(), principalDetails.getRole());

        redisService.setDataExpire(principalDetails.getLoginId(), token, JwtProvider.ACCESS_TOKEN_EXPIRE_TIME);

        response.addHeader(JwtProvider.TOKEN_HEADER_KEY, AUTH_TYPE + token);
    }
}
