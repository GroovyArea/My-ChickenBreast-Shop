package com.daniel.mychickenbreastshop.auth.security.config;

import com.daniel.mychickenbreastshop.auth.jwt.JwtTokenProvider;
import com.daniel.mychickenbreastshop.auth.security.filter.CustomAccessDeniedHandler;
import com.daniel.mychickenbreastshop.auth.security.filter.CustomAuthenticationEntryPoint;
import com.daniel.mychickenbreastshop.auth.security.filter.JwtAuthenticationFilter;
import com.daniel.mychickenbreastshop.auth.security.filter.JwtAuthorizationFilter;
import com.daniel.mychickenbreastshop.domain.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
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

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/resources/**"); // 테스트 시 path 수정할 것.
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .addFilter(new JwtAuthenticationFilter(authenticationManager()))
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), userRepository, jwtTokenProvider))

                .authorizeRequests()
                .antMatchers("/*/join", "/*/login").permitAll()
                .antMatchers(HttpMethod.GET, "/exception/**").permitAll()

                .antMatchers("/api/v1/**").hasRole("USER")
                .antMatchers("/api/v2/**").hasRole("ADMIN") // 테스트 시 path 관리할 것

                .and()
                .exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint())

                .and()
                .exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler());
    }
}
