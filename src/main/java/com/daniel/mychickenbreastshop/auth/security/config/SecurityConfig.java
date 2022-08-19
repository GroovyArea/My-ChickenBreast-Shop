package com.daniel.mychickenbreastshop.auth.security.config;

import com.daniel.mychickenbreastshop.auth.jwt.JwtAuthenticator;
import com.daniel.mychickenbreastshop.auth.jwt.JwtProvider;
import com.daniel.mychickenbreastshop.auth.jwt.JwtValidator;
import com.daniel.mychickenbreastshop.auth.security.application.PrincipalDetailService;
import com.daniel.mychickenbreastshop.auth.security.filter.JwtAuthenticationFilter;
import com.daniel.mychickenbreastshop.auth.security.filter.JwtAuthorizationFilter;
import com.daniel.mychickenbreastshop.auth.security.filter.custom.CustomAccessDeniedHandler;
import com.daniel.mychickenbreastshop.auth.security.filter.custom.CustomAuthenticationEntryPoint;
import com.daniel.mychickenbreastshop.auth.security.filter.custom.CustomAuthenticationProvider;
import com.daniel.mychickenbreastshop.domain.user.domain.UserRepository;
import com.daniel.mychickenbreastshop.global.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

/**
 * 서버 보안 설정 클래스
 *
 * <pre>
 *     <b>history</b>
 *     1.0, 2022.08.13 최초 작성
 * </pre>
 *
 * @author 김남영
 * @version 1.0
 */
@Slf4j
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtProvider jwtProvider;
    private final JwtValidator jwtValidator;
    private final JwtAuthenticator jwtAuthenticator;
    private final CorsConfig corsConfig;
    private final UserRepository userRepository;
    private final PrincipalDetailService principalDetailService;
    private final RedisService redisService;

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/static/**"); // 테스트 시 path 수정할 것.
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .addFilter(corsConfig.corsFilter())
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .httpBasic().disable()

                .authorizeRequests()
                .antMatchers("/login").permitAll()
                .antMatchers("/api/v1/**").hasAnyRole("USER", "ADMIN")
                .antMatchers("/api/v2/**").hasRole("ADMIN")// 테스트 시 path 관리할 것

                .and()
                .formLogin()
                .loginProcessingUrl("/login")

                .and()
                .addFilter(new JwtAuthenticationFilter(authenticationManager(), jwtProvider))
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), jwtProvider, jwtValidator, jwtAuthenticator))

                .exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .accessDeniedHandler(new CustomAccessDeniedHandler());
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        return new CustomAuthenticationProvider(userRepository, principalDetailService);
    }
}
