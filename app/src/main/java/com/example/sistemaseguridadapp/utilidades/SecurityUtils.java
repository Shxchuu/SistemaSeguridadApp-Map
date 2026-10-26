package com.example.sistemaseguridadapp.utilidades;

import android.util.Base64;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.regex.Pattern;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public final class SecurityUtils {
    private static final Pattern USERNAME = Pattern.compile("^[A-Za-z0-9_]{4,20}$");
    private static final int ITERACIONES = 120_000;
    private static final SecureRandom RANDOM = new SecureRandom();

    private SecurityUtils() { }

    public static boolean isValidUsername(String u) {
        return u != null && USERNAME.matcher(u).matches();
    }

    public static boolean isStrongPassword(String p) {
        if (p == null || p.length() < 8 || p.length() > 64) return false;
        boolean may = false, min = false, num = false;
        for (char c : p.toCharArray()) {
            if (Character.isUpperCase(c)) may = true;
            else if (Character.isLowerCase(c)) min = true;
            else if (Character.isDigit(c)) num = true;
        }
        return may && min && num;
    }

    public static String sanitize(String input) {
        if (input == null) return "";
        return input.replaceAll("[\\p{Cntrl}<>\"'`;\\\\]", "").trim();
    }

    public static byte[] newSalt() {
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        return salt;
    }

    public static String hashPassword(String password, byte[] salt) {
        try {
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERACIONES, 256);
            SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            return Base64.encodeToString(f.generateSecret(spec).getEncoded(), Base64.NO_WRAP);
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Error al calcular el hash", e);
        }
    }

    public static boolean constantTimeEquals(String a, String b) {
        return MessageDigest.isEqual(a.getBytes(), b.getBytes());
    }

    public static String newToken() {
        byte[] t = new byte[32];
        RANDOM.nextBytes(t);
        return Base64.encodeToString(t, Base64.URL_SAFE | Base64.NO_WRAP | Base64.NO_PADDING);
    }
}