package com.daniel.mychickenbreastshop.auth.security.filter;

import com.daniel.mychickenbreastshop.domain.user.dto.request.LoginRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

    private final AuthenticationManager authenticationManager;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        LoginRequestDto loginRequestDto = extractDto(request);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginRequestDto.getLoginId(), loginRequestDto.getPassword());

        return authenticationManager.authenticate(authenticationToken);
    }

    private LoginRequestDto extractDto(HttpServletRequest request) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(request.getInputStream(), LoginRequestDto.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
