package com.daniel.mychickenbreastshop.user.auth.jwt;

import com.daniel.mychickenbreastshop.user.auth.jwt.enums.JwtProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.Key;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtProviderTest {

    private static final long EXPIRED_TIME = 30 * 60 * 1000L; // 30분
    private static final String SECRET_KEY = "mysecretmysecretmysecretmysecretmysecretmysecretmysecretmysecretmysecretmysecret";
    private static final String USER_PK = "1L";
    private static final String LOGIN_ID = "loginId";
    private static final String ROLE = "ROLE_USER";

    private JwtProvider jwtProvider;


    private String token;

    @BeforeEach
    void setUp() {
        jwtProvider = new JwtProvider();
        ReflectionTestUtils.setField(jwtProvider, "secretKey", SECRET_KEY);

        Date now = new Date();
        Date validity = new Date(now.getTime() + EXPIRED_TIME);

        Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

        token = Jwts.builder()
                .claim("id", USER_PK)
                .claim("loginId", LOGIN_ID)
                .claim("role", ROLE)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    @DisplayName("토큰을 생성한다.")
    @Test
    void createToken() {
        // when
        String userPk1 = (String) Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get(JwtProperties.ID.getKey());

        assertThat(USER_PK).isEqualTo(userPk1);
    }

    @DisplayName("토큰의 유효 기간 날짜, 시간을 반환한다.")
    @Test
    void getExpireDate() {
        // when
        Date expireDate = jwtProvider.getExpireDate(token);

        assertThat(new Date().getTime()).isLessThan(expireDate.getTime());
    }


    @DisplayName("request 헤더에서 토큰을 추출한다.")
    @Test
    void getResolvedToken() {
        // when
        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        mockHttpServletRequest.addHeader("Authorization", "Bearer " + token);
        String extractedToken = jwtProvider.getResolvedToken(mockHttpServletRequest, "Bearer ");

        assertThat(extractedToken).isEqualTo(token);
    }

    @DisplayName("토큰 바디에서 userPk를 추출한다.")
    @Test
    void getUserPk() {
        // when
        String userPk = jwtProvider.getUserPk(token);

        assertThat(userPk).isEqualTo(USER_PK);
    }

    @DisplayName("토큰 바디에서 loginId를 추출한다.")
    @Test
    void getLoginId() {
        // when
        String loginId = jwtProvider.getLoginId(token);

        assertThat(loginId).isEqualTo(LOGIN_ID);
    }


}