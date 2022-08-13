package com.daniel.mychickenbreastshop.domain.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Date;
import java.util.List;

/**
 * 토큰 제공 클래스 <br>
 * 토큰 생성, 유효성 검사, 값 추출
 *
 * <pre>
 *     <History>
 *         1.0 2022.05 최초 작성
 *         1.1 2022.08.01 리팩토링 (deprecated 메서드 최신 메서드로 수정)
 *         1.2 2022.08.11 리팩토링 (토큰 추출 메서드 추가)
 *     </History>
 *
 * </pre>
 *
 * @author 김남영
 * @version 1.2
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider {

    @Value("${jwt.secret.key}")
    private String secretKey;
    private static final long VALIDATE_IN_MILLISECONDS = 1000 * 60L * 30L;
    public static final String TOKEN_HEADER_KEY = "X-AUTH-TOKEN";

    private final UserDetailsService userDetailsService;

    /**
     * 토큰 생성
     *
     * @param userPk 유저 PK
     * @param roles  권한들
     * @return 생성된 토큰
     */
    public String createToken(String userPk, List<String> roles) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + VALIDATE_IN_MILLISECONDS);

        log.info("now: {}", now);
        log.info("validity: {}", validity);

        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        Key key = Keys.hmacShaKeyFor(keyBytes);

        Claims claims = Jwts.claims().setSubject(userPk);
        claims.put("roles", roles);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 토큰 유효성 검사
     *
     * @param token 토큰
     * @return 논리 값
     */
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts
                    .parserBuilder()
                    .setSigningKey(secretKey.getBytes())
                    .build()
                    .parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 토큰 만료 시간 추출
     *
     * @param token 토큰
     * @return 토큰 만료 시간
     */
    public Date getExpireDate(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }

    /**
     * 헤더에서 토큰 추출
     *
     * @param request request 객체
     * @return 토큰
     */
    public String getResolvedToken(HttpServletRequest request) {
        return request.getHeader(TOKEN_HEADER_KEY);
    }

    /**
     * 토큰으로 인증 정보 조회
     *
     * @param token 토큰
     * @return 인증 정보
     */
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getUserPk(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    /**
     * 토큰에서 회원 구별 정보 추출
     *
     * @param token 토큰
     * @return 회원 구별 정보
     */
    public String getUserPk(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
