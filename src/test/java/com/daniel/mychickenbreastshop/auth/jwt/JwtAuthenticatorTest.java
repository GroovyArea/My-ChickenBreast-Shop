package com.daniel.mychickenbreastshop.auth.jwt;

import com.daniel.mychickenbreastshop.auth.security.model.PrincipalDetails;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.Key;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticatorTest {

    private static final long EXPIRED_TIME = 30 * 60 * 1000L; // 30분
    private static final String SECRET_KEY = "mysecretmysecretmysecretmysecretmysecretmysecretmysecretmysecretmysecretmysecret";
    private static final String USER_PK = "1L";
    private static final String LOGIN_ID = "loginId";
    private static final String ROLE = "ROLE_USER";

    @Mock
    private UserDetailsService userDetailsService;

    @InjectMocks
    private JwtAuthenticator jwtAuthenticator;

    private String token;
    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtAuthenticator, "secretKey", SECRET_KEY);

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

    @DisplayName("UserDetails 객체를 반환한다.")
    @Test
    void getAuthentication() {
        // given
        UserDetails userDetails = PrincipalDetails.builder()
                .id(1L)
                .loginId(LOGIN_ID)
                .name("name")
                .password("password")
                .role(ROLE)
                .build();

        // when
        when(userDetailsService.loadUserByUsername(LOGIN_ID)).thenReturn(userDetails);
        Authentication authentication = jwtAuthenticator.getAuthentication(token);

        assertThat(authentication.getPrincipal()).isEqualTo(userDetails);
    }
}