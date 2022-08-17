package com.daniel.mychickenbreastshop.auth.security.filter.custom;

import com.daniel.mychickenbreastshop.auth.security.application.PrincipalDetailService;
import com.daniel.mychickenbreastshop.auth.security.model.PrincipalDetails;
import com.daniel.mychickenbreastshop.domain.user.domain.User;
import com.daniel.mychickenbreastshop.domain.user.domain.UserRepository;
import com.daniel.mychickenbreastshop.global.util.PasswordEncrypt;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.security.NoSuchAlgorithmException;

/**
 * AuthenticationProvider 커스터마이징
 * 비밀번호 암호화 첨가물 작업 및 비교
 */
@AllArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private UserRepository userRepository;
    private PrincipalDetailService principalDetailService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String loginId = (String) authentication.getPrincipal();
        String loginPassword = (String) authentication.getCredentials();

        User dbUser = userRepository.findByLoginId(loginId).orElseThrow(() -> new RuntimeException("회원이 존재하지 않습니다."));
        String dbPassword = dbUser.getPassword();
        String dbSalt = dbUser.getSalt();

        try {
            String encryptedLoginPassword = PasswordEncrypt.getSecurePassword(loginPassword, dbSalt);

            if (!encryptedLoginPassword.equals(dbPassword)) {
                throw new BadCredentialsException(loginId);
            }

            PrincipalDetails principalDetails = (PrincipalDetails) principalDetailService.loadUserByUsername(loginId);

            return new UsernamePasswordAuthenticationToken(principalDetails, principalDetails.getPassword(), principalDetails.getAuthorities());

        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
