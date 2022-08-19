package com.daniel.mychickenbreastshop.auth.security.filter;

import com.daniel.mychickenbreastshop.auth.jwt.JwtAuthenticator;
import com.daniel.mychickenbreastshop.auth.jwt.JwtProvider;
import com.daniel.mychickenbreastshop.auth.jwt.JwtValidator;
import com.daniel.mychickenbreastshop.auth.jwt.model.ErrorMessages;
import com.daniel.mychickenbreastshop.auth.jwt.model.JwtProperties;
import com.daniel.mychickenbreastshop.auth.security.model.PrincipalDetails;
import com.daniel.mychickenbreastshop.global.service.RedisService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final JwtProvider jwtProvider;
    private final JwtValidator jwtValidator;
    private final JwtAuthenticator jwtAuthenticator;
    private final RedisService redisService;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, JwtProvider jwtProvider, JwtValidator jwtValidator, JwtAuthenticator jwtAuthenticator, RedisService redisService) {
        super(authenticationManager);
        this.jwtProvider = jwtProvider;
        this.jwtValidator = jwtValidator;
        this.jwtAuthenticator = jwtAuthenticator;
        this.redisService = redisService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String header = request.getHeader(JwtProperties.TOKEN_HEADER_KEY.getKey());

        if (header == null || !header.startsWith(JwtProperties.AUTH_TYPE.getKey())) {
            chain.doFilter(request, response);
            return;
        }

        String token = jwtProvider.getResolvedToken(request, JwtProperties.AUTH_TYPE.getKey());

        Authentication authentication = getAuthentication(token);

        if (token != null && jwtValidator.validateAccessToken(token)) {
            if(!redisService.getData((String) authentication.getPrincipal()).equals(token)) {
                throw new RuntimeException(ErrorMessages.TOKEN_MISMATCH.getMessage());
            }
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(request, response);
    }

    private Authentication getAuthentication(String token) {
        PrincipalDetails principalDetails = (PrincipalDetails) jwtAuthenticator.getAuthentication(token).getPrincipal();

        return new UsernamePasswordAuthenticationToken(
                principalDetails.getLoginId(), principalDetails.getPassword(), principalDetails.getAuthorities()
        );
    }
}
