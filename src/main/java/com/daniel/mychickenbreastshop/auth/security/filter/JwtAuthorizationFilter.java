package com.daniel.mychickenbreastshop.auth.security.filter;

import com.daniel.mychickenbreastshop.auth.jwt.JwtProvider;
import com.daniel.mychickenbreastshop.auth.security.model.PrincipalDetails;
import com.daniel.mychickenbreastshop.domain.user.domain.User;
import com.daniel.mychickenbreastshop.domain.user.domain.UserRepository;
import com.daniel.mychickenbreastshop.domain.user.enums.ResponseMessages;
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

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    public static final String AUTH_TYPE = "Bearer ";


    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository, JwtProvider jwtProvider) {
        super(authenticationManager);
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        if(request.getRequestURI().startsWith("/join")) {
            chain.doFilter(request, response);
            return;
        }

        String header = request.getHeader(JwtProvider.TOKEN_HEADER_KEY);

        if (header == null || !header.startsWith(AUTH_TYPE)) {
            chain.doFilter(request, response);
            return;
        }

        Authentication authentication = getAuthentication(request);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        chain.doFilter(request, response);
    }

    private Authentication getAuthentication(HttpServletRequest request) {
        String token = jwtProvider.getResolvedToken(request, AUTH_TYPE);
        String userPk = jwtProvider.getUserPk(token);

        User dbUser = userRepository.findById(Long.valueOf(userPk)).orElseThrow(() -> new RuntimeException(ResponseMessages.USER_NOT_EXISTS_MESSAGE.getMessage()));

        PrincipalDetails principalDetails = PrincipalDetails.builder()
                .id(dbUser.getId())
                .name(dbUser.getName())
                .loginId(dbUser.getLoginId())
                .password(dbUser.getPassword())
                .role(dbUser.getRoleType().getRoleName())
                .build();

        return new UsernamePasswordAuthenticationToken(
                principalDetails, principalDetails.getPassword(), principalDetails.getAuthorities()
        );

    }
}
