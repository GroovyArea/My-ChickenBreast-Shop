package com.daniel.mychickenbreastshop.global.auth.filter;


import com.daniel.mychickenbreastshop.global.auth.enums.FilterMessage;
import com.daniel.mychickenbreastshop.global.auth.jwt.AuthorizationExtractor;
import com.daniel.mychickenbreastshop.global.auth.jwt.JwtTokenProvider;
import com.daniel.mychickenbreastshop.global.error.exception.RedisNullTokenException;
import com.daniel.mychickenbreastshop.global.error.exception.TokenMismatchException;
import com.daniel.mychickenbreastshop.global.service.RedisService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JwtFilter 클래스
 *
 * <pre>
 *     <History>
 *         1.0 2022.05 최초 작성
 *         1.1 2022.08.02 리팩토링 (응답 메시지 enum 매핑)
 *     </History>
 *
 * </pre>
 * @author 김남영
 * @version 1.1
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private static final String BEARER_TOKEN = "Bearer";
    private static final String ENCODE_TYPE = "utf-8";

    private final AuthorizationExtractor authorizationExtractor;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();

        if (!requestURI.startsWith("/api")) {
            filterChain.doFilter(request, response);
        }

        try {
            String requestToken = authorizationExtractor.extract(request, BEARER_TOKEN);
            jwtTokenProvider.validateToken(requestToken);

            final String tokenUserId = jwtTokenProvider.getUserId(requestToken);

            /* request에 토큰 유저 권한 및 아이디 추가 */
            request.setAttribute("tokenUserRole", jwtTokenProvider.getUserGrade(requestToken));
            request.setAttribute("tokenUserId", jwtTokenProvider.getUserId(requestToken));
            request.setAttribute("token", requestToken);

            /* Redis DB에 저장된 토큰 추출 */
            final String redisToken = redisService.getData(tokenUserId);

            /* DB에 토큰이 존재하지 않을 경우 */
            if (redisToken == null) {
                throw new RedisNullTokenException(FilterMessage.NULL_TOKEN.getMessage());
            }

            /* DB 토큰과 로그인 유저 토큰 정보가 일치하지 않을 경우 */
            if (!redisToken.equals(requestToken)) {
                throw new TokenMismatchException(FilterMessage.INVALID_TOKEN.getMessage());
            }

            filterChain.doFilter(request, response);

        } catch (SignatureException e) {
            setErrorResponse(response, FilterMessage.INVALID_SIGNATURE.getMessage(), e);
        } catch (RedisNullTokenException e) {
            setErrorResponse(response, FilterMessage.NULL_TOKEN.getMessage(), e);
        } catch (TokenMismatchException e) {
            setErrorResponse(response, FilterMessage.INVALID_TOKEN.getMessage(), e);
        } catch (ClassCastException e) {
            setErrorResponse(response, FilterMessage.CLASS_CAST_FAIL.getMessage(), e);
        } catch (MalformedJwtException e) {
            setErrorResponse(response, FilterMessage.MALFORMED_TOKEN.getMessage(), e);
        } catch (ExpiredJwtException e) {
            setErrorResponse(response, FilterMessage.EXPIRED_TOKEN.getMessage(), e);
        } catch (UnsupportedJwtException e) {
            setErrorResponse(response, FilterMessage.UNSUPPORTED_TOKEN.getMessage(), e);
        } catch (Exception e) {
            setErrorResponse(response, e.getMessage(), e);
        }
    }

    private void setErrorResponse(HttpServletResponse response, String message, Exception e) throws IOException {
        log.error(e.getMessage());
        response.setCharacterEncoding(ENCODE_TYPE);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getWriter().write(message);
    }
}
