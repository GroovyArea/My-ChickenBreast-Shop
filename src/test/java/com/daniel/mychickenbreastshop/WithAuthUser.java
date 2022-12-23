package com.daniel.mychickenbreastshop;

import com.daniel.mychickenbreastshop.user.domain.enums.Role;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithAuthUserSecurityContextFactory.class)
public @interface WithAuthUser {

    long id() default 1L;
    String loginId() default "loginId";
    String name() default "name";
    String password() default "password";
    String email() default "0909@gmail.com";
    String address() default "서울시 서초구 서초동";
    String zipcode() default "123-123";
    Role role() default Role.ROLE_USER;
}
