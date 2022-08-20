package com.daniel.mychickenbreastshop.auth.jwt;

import com.daniel.mychickenbreastshop.auth.jwt.model.JwtErrorMessage;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 토큰 유효성 검사 클래스
 *
 * <pre>
 *     <b>history</b>
 *     1.0 2022.08.17 최초 작성
 *
 * </pre>
 *
 * @author Daniel Kim
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class JwtValidator {

    @Value("${spring.jwt.key}")
    private String secretKey;

    public boolean validateAccessToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey.getBytes()).build().parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException e) {
            throw new IllegalArgumentException(JwtErrorMessage.MALFORMED.getMessage());
        } catch (ExpiredJwtException e) {
            throw new IllegalArgumentException(JwtErrorMessage.EXPIRED.getMessage());
        } catch (UnsupportedJwtException e) {
            throw new IllegalArgumentException(JwtErrorMessage.UNSUPPORTED.getMessage());
        } catch (ClassCastException e) {
            throw new IllegalArgumentException(JwtErrorMessage.CLASS_CAST_FAIL.getMessage());
        } catch (SignatureException e) {
            throw new IllegalArgumentException(JwtErrorMessage.INVALID_SIGNATURE.getMessage());
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

}
