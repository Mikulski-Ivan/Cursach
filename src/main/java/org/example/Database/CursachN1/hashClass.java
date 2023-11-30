package org.example.Database.CursachN1;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class hashClass {
    public static String getSecurePasswordWithSalt(String password, String salt) {

        byte[] hashSalt=salt.getBytes(StandardCharsets.UTF_8);
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA3-256");
            md.update(hashSalt);
            byte[] bytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    public static String getSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return new String(salt, StandardCharsets.UTF_8);
    }
}
