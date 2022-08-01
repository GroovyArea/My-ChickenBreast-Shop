package com.daniel.mychickenbreastshop.global.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * 토큰 제공 클래스 <br>
 * 토큰 생성, 유효성 검사, 값 추출
 *
 * <pre>
 *     <History>
 *         1.0 2022.05 최초 작성
 *         1.1 2022.08.01 리팩토링 (deprecated 메서드 최신 메서드로 수정)
 *     </History>
 *
 * </pre>
 * @author 김남영
 * @version 1.1
 */
@Component
@Slf4j
public class JwtTokenProvider {

    @Value("${jwt.secret.key}")
    private String secretKey;
    private static final long VALIDATE_IN_MILLISECONDS = 1000 * 60L * 30L;

    /**
     * 토큰 생성
     *
     * @param id 유저 아이디
     * @return 토큰 값
     */
    public String createToken(String id, String grade) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + VALIDATE_IN_MILLISECONDS);
        log.info("now: {}", now);
        log.info("validity: {}", validity);

        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        Key key = Keys.hmacShaKeyFor(keyBytes);

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject("Login Token")
                .claim("userId", id)
                .claim("userGrade", grade)
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
        Jws<Claims> claims = Jwts
                .parserBuilder()
                .setSigningKey(secretKey.getBytes())
                .build()
                .parseClaimsJws(token);
        return !claims.getBody().getExpiration().before(new Date());
    }

    /**
     * 토큰에서 아이디 값 추출
     *
     * @param token 토큰
     * @return 로그인 유저 아이디
     */
    public String getUserId(String token) {
        return (String) Jwts.parserBuilder()
                .setSigningKey(secretKey.getBytes())
                .requireAudience(token)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("userId");
    }

    /**
     * 토큰에서 유저 등급 추출
     *
     * @param token 토큰
     * @return 로그인 유저 등급
     */
    public String getUserGrade(String token) {
        return (String) Jwts.parserBuilder()
                .setSigningKey(secretKey.getBytes())
                .requireAudience(token)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("userGrade");
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

}
