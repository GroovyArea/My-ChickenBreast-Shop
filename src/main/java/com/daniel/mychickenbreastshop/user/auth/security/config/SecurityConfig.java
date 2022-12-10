package com.daniel.mychickenbreastshop.user.auth.security.config;

import com.daniel.mychickenbreastshop.user.adaptor.out.persistence.UserRepository;
import com.daniel.mychickenbreastshop.user.auth.jwt.JwtAuthenticator;
import com.daniel.mychickenbreastshop.user.auth.jwt.JwtProvider;
import com.daniel.mychickenbreastshop.user.auth.jwt.JwtValidator;
import com.daniel.mychickenbreastshop.user.auth.security.service.PrincipalDetailService;
import com.daniel.mychickenbreastshop.user.auth.security.error.handler.CustomAccessDeniedHandler;
import com.daniel.mychickenbreastshop.user.auth.security.error.handler.CustomAuthenticationEntryPoint;
import com.daniel.mychickenbreastshop.user.auth.security.filter.JwtAuthenticationFilter;
import com.daniel.mychickenbreastshop.user.auth.security.filter.custom.CustomAuthenticationFilter;
import com.daniel.mychickenbreastshop.user.auth.security.filter.custom.CustomAuthenticationProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 서버 보안 설정 클래스
 *
 * <pre>
 *     <b>history</b>
 *     1.0, 2022.08.13 최초 작성
 *     1.1, 2022.10.14 Deprecated 클래스 제거 및 최신화
 * </pre>
 *
 * @author 김남영
 * @version 1.1
 */
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtProvider jwtProvider;
    private final JwtValidator jwtValidator;
    private final JwtAuthenticator jwtAuthenticator;
    private final UserRepository userRepository;
    private final PrincipalDetailService principalDetailService;
    private final ObjectMapper objectMapper;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .antMatchers("/static/**",
                        "/db/**",
                        "/template/**");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager, CorsFilter corsFilter) throws Exception {
        http
                .addFilter(corsFilter())
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .httpBasic().disable()
                .formLogin().disable()

                .authorizeRequests()
                .antMatchers("/api/v1/**").hasAnyRole("USER", "ADMIN")
                .antMatchers("/api/v2/**").hasRole("ADMIN")// 테스트 시 path 관리할 것
                .anyRequest().permitAll()

                .and()
                .addFilter(new CustomAuthenticationFilter(authenticationManager, jwtProvider, objectMapper))
                .addFilter(new JwtAuthenticationFilter(authenticationManager, jwtProvider, jwtValidator, jwtAuthenticator))

                .exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .accessDeniedHandler(new CustomAccessDeniedHandler());

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        return new CustomAuthenticationProvider(userRepository, principalDetailService);
    }

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.addAllowedOriginPattern("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return new CorsFilter(source);
    }
}
