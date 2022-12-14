package com.daniel.mychickenbreastshop.global.util;

import lombok.experimental.UtilityClass;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

@UtilityClass
public class PasswordEncrypt {

    public static String getSalt() {
        Random random = new SecureRandom();
        byte[] salt = new byte[10];

        random.nextBytes(salt);

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < salt.length; i++) {
            sb.append(String.format("%02x", salt[i]));
        }

        return sb.toString();
    }

    public static String getSecurePassword(String pwd, String salt) throws NoSuchAlgorithmException {

        byte[] bytesArrOfSalt = salt.getBytes();
        String result;

        byte[] temp = pwd.getBytes();
        byte[] bytes = new byte[temp.length + bytesArrOfSalt.length];

        System.arraycopy(temp, 0, bytes, 0, temp.length);
        System.arraycopy(salt.getBytes(), 0, bytes, temp.length, salt.getBytes().length);

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(bytes);

        byte[] b = md.digest();

        StringBuilder sb = new StringBuilder();

        for (byte value : b) {
            sb.append(Integer.toString((value & 0xFF) + 256, 16).substring(1));
        }

        result = sb.toString();

        return result;
    }

}
