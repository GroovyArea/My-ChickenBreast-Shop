package com.daniel.mychickenbreastshop.auth.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Date;
import java.util.Enumeration;

/**
 * 토큰 제공 클래스 <br>
 * 토큰 생성, 유효성 검사, 값 추출
 *
 * <pre>
 *     <History>
 *         1.0 2022.05 최초 작성
 *         1.1 2022.08.01 리팩토링 (deprecated 메서드 최신 메서드로 수정)
 *         1.2 2022.08.11 리팩토링 (토큰 추출 메서드 추가)
 *         1.3 2022.08.17 리팩토링 (책임 분리)
 *     </History>
 *
 * </pre>
 *
 * @author Daniel Kim
 * @version 1.3
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtProvider {

    @Value("${spring.jwt.key}")
    private String secretKey;
    public static final long ACCESS_TOKEN_EXPIRE_TIME = 30 * 60 * 1000L; // 30분
    public static final String TOKEN_HEADER_KEY = "Authorization";
    private static final String ROLE = "role";
    private static final String ID = "id";
    private static final String LOGIN_ID = "loginId";

    /**
     * 토큰 생성
     *
     * @param userPk 유저 PK
     * @param role   권한
     * @return 생성된 토큰
     */
    public String createToken(String userPk, String loginId, String role) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_TIME);

        log.info("now: {}", now);
        log.info("validity: {}", validity);

        //byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        Key key = getSignKey();

        return Jwts.builder()
                .claim(ID, userPk)
                .claim(LOGIN_ID, loginId)
                .claim(ROLE, role)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSignKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
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
    public String getResolvedToken(HttpServletRequest request, String type) {
        Enumeration<String> headers = request.getHeaders(TOKEN_HEADER_KEY);
        while (headers.hasMoreElements()) {
            String value = headers.nextElement();
            if (value.toLowerCase().startsWith(type.toLowerCase())) {
                return value.substring(type.length()).trim();
            }
        }
        return Strings.EMPTY;
    }

    /**
     * 토큰에서 회원 구별 정보 추출
     *
     * @param token 토큰
     * @return 회원 구별 정보
     */
    public String getUserPk(String token) {
        return (String) Jwts.parserBuilder()
                .setSigningKey(secretKey.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get(ID);
    }

    public String getLoginId(String token) {
        return (String) Jwts.parserBuilder()
                .setSigningKey(secretKey.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get(LOGIN_ID);
    }
}
