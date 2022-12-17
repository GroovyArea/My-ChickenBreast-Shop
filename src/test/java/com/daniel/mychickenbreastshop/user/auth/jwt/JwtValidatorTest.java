package com.daniel.mychickenbreastshop.user.auth.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.Key;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtValidatorTest {

    private static final String SECRET_KEY = "mysecretmysecretmysecretmysecretmysecretmysecretmysecretmysecretmysecretmysecret";
    private static final String USER_PK = "1L";
    private static final String LOGIN_ID = "loginId";
    private static final String ROLE = "ROLE_USER";
    private JwtValidator jwtValidator;

    @BeforeEach
    void setUp() {
        jwtValidator = new JwtValidator();
        ReflectionTestUtils.setField(jwtValidator, "secretKey", SECRET_KEY);
    }

    @DisplayName("토큰 유효성 검사가 성공할 경우 True를 반환한다.")
    @Test
    void validateToken() {
        // given
        String token = makeToken(60 * 1000L);

        assertThat(jwtValidator.validateAccessToken(token)).isTrue();
    }

    @DisplayName("만료 시간이 지난 토큰을 체크할 경우 예외를 발생시킨다.")
    @Test
    void validateTokenExpired() {
        // given
        String token = makeToken(1L); // 지속시간 0.001초

        assertThatThrownBy(() -> jwtValidator.validateAccessToken(token))
                .isInstanceOf(IllegalArgumentException.class).hasMessage("토큰 유효기간이 만료되었습니다. 재로그인 요망.");
    }

    @DisplayName("지원하지 않는 토큰 형식의 경우 예외를 발생시킨다.")
    @Test
    void validateTokenMalformed() {
        String malformedToken = "blblblblah";

        assertThatThrownBy(() -> jwtValidator.validateAccessToken(malformedToken))
                .isInstanceOf(IllegalArgumentException.class).hasMessage("유효하지 않은 토큰입니다.");
    }

    private String makeToken(long expiredTime) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + expiredTime);

        Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

        return Jwts.builder()
                .claim("id", USER_PK)
                .claim("loginId", LOGIN_ID)
                .claim("role", ROLE)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
