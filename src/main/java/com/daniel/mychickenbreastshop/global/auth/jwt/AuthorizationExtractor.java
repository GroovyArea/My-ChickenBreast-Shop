package com.daniel.mychickenbreastshop.global.auth.jwt;

import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * 토큰 추출 클래스 <br>
 * 헤더에서 토큰 추출
 *
 * <pre>
 *     <History>
 *         1.0 2022.05 최초 작성
 *     </History>
 *
 * </pre>
 * @author 김남영
 * @version 1.0
 */
@Component
public class AuthorizationExtractor {

    public static final String AUTHORIZATION = "Authorization";
    public static final String ACCESS_TOKEN_TYPE = AuthorizationExtractor.class.getSimpleName();

    public String extract(HttpServletRequest request, String type) {
        Enumeration<String> headers = request.getHeaders(AUTHORIZATION);
        while (headers.hasMoreElements()) {
            String value = headers.nextElement();
            if (value.toLowerCase().startsWith(type.toLowerCase())) {
                return value.substring(type.length()).trim();
            }
        }
        return Strings.EMPTY;
    }
}
