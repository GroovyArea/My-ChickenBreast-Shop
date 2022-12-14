package com.daniel.mychickenbreastshop.user.auth.security.filter.custom;

import com.daniel.mychickenbreastshop.global.error.exception.BadRequestException;
import com.daniel.mychickenbreastshop.global.util.PasswordEncrypt;
import com.daniel.mychickenbreastshop.user.adaptor.out.persistence.UserRepository;
import com.daniel.mychickenbreastshop.user.auth.security.service.PrincipalDetailService;
import com.daniel.mychickenbreastshop.user.auth.security.error.SecurityMessages;
import com.daniel.mychickenbreastshop.user.auth.security.model.PrincipalDetails;
import com.daniel.mychickenbreastshop.user.domain.User;
import com.daniel.mychickenbreastshop.user.domain.enums.ErrorMessages;
import com.daniel.mychickenbreastshop.user.domain.enums.Role;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

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

        User dbUser = userRepository.findByLoginId(loginId).orElseThrow(() -> new UsernameNotFoundException(ErrorMessages.USER_NOT_EXISTS.getMessage()));
        String dbPassword = dbUser.getPassword();
        String dbSalt = dbUser.getSalt();

        try {
            String encryptedLoginPassword = PasswordEncrypt.getSecurePassword(loginPassword, dbSalt);

            if (!encryptedLoginPassword.equals(dbPassword)) {
                throw new BadCredentialsException(SecurityMessages.PASSWORD_MISMATCH.getMessage());
            }

            if (dbUser.getRole() == Role.ROLE_WITHDRAWAL) {
                throw new BadRequestException(ErrorMessages.WITHDRAW_USER.getMessage());
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
