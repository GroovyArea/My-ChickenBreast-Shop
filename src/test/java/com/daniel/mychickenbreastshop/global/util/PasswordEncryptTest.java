package com.daniel.mychickenbreastshop.global.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;

class PasswordEncryptTest {

    @DisplayName("암호화된 password를 반환한다.")
    @Test
    void getSecuredPasswordTest() throws NoSuchAlgorithmException {
        String salt = PasswordEncrypt.getSalt();
        String password = "1234";
        String securedPassword = PasswordEncrypt.getSecurePassword(password, salt);
// 1234 해쉬 문자열
        Assertions.assertThat(securedPassword).isNotNull();
    }
}