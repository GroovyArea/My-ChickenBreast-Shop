package com.daniel.mychickenbreastshop.global.util;

import com.daniel.mychickenbreastshop.user.auth.security.model.PrincipalDetails;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.context.SecurityContextHolder;

@UtilityClass
public class PrincipalUtil {

    private static final PrincipalDetails PRINCIPAL_DETAILS = (PrincipalDetails) SecurityContextHolder.getContext()
            .getAuthentication()
            .getPrincipal();

    public static Long getCurrentId() {
        return PRINCIPAL_DETAILS.getId();
    }

    public static String getCurrentLoginId() {
        return PRINCIPAL_DETAILS.getLoginId();
    }
}
