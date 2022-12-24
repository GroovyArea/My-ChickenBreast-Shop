package com.daniel.mychickenbreastshop.user.auth.jwt;

import com.daniel.mychickenbreastshop.user.auth.jwt.enums.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

/**
 * jwt로 authentication 객체를 가져오는 클래스
 *
 * <pre>
 *     <b>history</b>
 *     1.0 2022.08.17 최초 작성
 * </pre>
 *
 * @author Daniel Kim
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticator {

    @Value("${spring.jwt.key}")
    private String secretKey;

    private final UserDetailsService userDetailsService;

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);

        UserDetails userDetails = userDetailsService.loadUserByUsername(
                (String) claims.get(JwtProperties.LOGIN_ID.getKey()));

        return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),
                userDetails.getAuthorities());
    }
}
