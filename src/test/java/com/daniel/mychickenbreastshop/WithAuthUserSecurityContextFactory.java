package com.daniel.mychickenbreastshop;

import com.daniel.mychickenbreastshop.user.auth.security.model.PrincipalDetails;
import com.daniel.mychickenbreastshop.user.domain.enums.Role;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.List;

public class WithAuthUserSecurityContextFactory implements WithSecurityContextFactory<WithAuthUser> {

    @Override
    public SecurityContext createSecurityContext(WithAuthUser annotation) {
        List<GrantedAuthority> role =
                AuthorityUtils.createAuthorityList(Role.ROLE_USER.name());

        PrincipalDetails principalDetails = PrincipalDetails.builder()
                .id(annotation.id())
                .loginId(annotation.loginId())
                .password(annotation.password())
                .name(annotation.name())
                .role(annotation.role().name())
                .build();

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(principalDetails, principalDetails.getPassword(),
                        role));

        return SecurityContextHolder.getContext();
    }
}
