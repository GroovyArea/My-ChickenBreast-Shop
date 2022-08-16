package com.daniel.mychickenbreastshop.auth.jwt;

import com.daniel.mychickenbreastshop.auth.security.error.exception.CustomAuthenticationEntrypointException;
import io.jsonwebtoken.*;
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

    @Value("${spring.jwt.key}")
    private String secretKey;
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 30 * 60 * 1000L; // 30분
    public static final String TOKEN_HEADER_KEY = "Authorization";
    private static final String ROLES = "roles";

    private final UserDetailsService userDetailsService;

    /**
     * 토큰 생성
     *
     * @param userPk 유저 PK
     * @param role  권한
     * @return 생성된 토큰
     */
    public String createToken(String userPk, String role) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_TIME);

        log.info("now: {}", now);
        log.info("validity: {}", validity);

        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        Key key = Keys.hmacShaKeyFor(keyBytes);

        return Jwts.builder()
                .setSubject(userPk)
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
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
        // Jwt 에서 claims 추출
        Claims claims = parseClaims(token);

        // 권한 정보가 없음
        if (claims.get(ROLES) == null) {
            throw new CustomAuthenticationEntrypointException();
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(getUserPk(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // Jwt 토큰 복호화해서 가져오기
    private Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
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

    // jwt 의 유효성 및 만료일자 확인
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.error("잘못된 Jwt 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.error("만료된 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("지원하지 않는 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.error("잘못된 토큰입니다.");
        }
        return false;
    }
}
