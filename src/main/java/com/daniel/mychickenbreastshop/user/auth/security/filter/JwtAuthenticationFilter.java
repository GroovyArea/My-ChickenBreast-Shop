package com.daniel.mychickenbreastshop.user.auth.security.filter;

import com.daniel.mychickenbreastshop.user.auth.jwt.JwtProvider;
import com.daniel.mychickenbreastshop.user.auth.jwt.enums.JwtProperties;
import com.daniel.mychickenbreastshop.user.auth.security.error.SecurityMessages;
import com.daniel.mychickenbreastshop.user.auth.security.filter.enums.PassablePathProperty;
import com.daniel.mychickenbreastshop.user.auth.security.model.PrincipalDetails;
import com.daniel.mychickenbreastshop.user.auth.jwt.JwtAuthenticator;
import com.daniel.mychickenbreastshop.user.auth.jwt.JwtValidator;
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
import java.util.Arrays;

public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    private final JwtProvider jwtProvider;
    private final JwtValidator jwtValidator;
    private final JwtAuthenticator jwtAuthenticator;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtProvider jwtProvider, JwtValidator jwtValidator, JwtAuthenticator jwtAuthenticator) {
        super(authenticationManager);
        this.jwtProvider = jwtProvider;
        this.jwtValidator = jwtValidator;
        this.jwtAuthenticator = jwtAuthenticator;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader(JwtProperties.TOKEN_HEADER_KEY.getKey());

        if (header == null || !header.startsWith(JwtProperties.AUTH_TYPE.getKey())) {
            request.setAttribute("exception", SecurityMessages.HEADER_LESS.getMessage());
            chain.doFilter(request, response);
            return;
        }

        String token = jwtProvider.getResolvedToken(request, JwtProperties.AUTH_TYPE.getKey());

        try {
            if (token != null && jwtValidator.validateAccessToken(token)) {
                Authentication authentication = getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            request.setAttribute("exception", e.getMessage());
        }

        chain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return Arrays.stream(PassablePathProperty.values()).map(PassablePathProperty::getPath).toList().contains(path);
    }

    private Authentication getAuthentication(String token) {
        PrincipalDetails principalDetails = (PrincipalDetails) jwtAuthenticator.getAuthentication(token).getPrincipal();

        return new UsernamePasswordAuthenticationToken(
                principalDetails, principalDetails.getPassword(), principalDetails.getAuthorities()
        );
    }
}
