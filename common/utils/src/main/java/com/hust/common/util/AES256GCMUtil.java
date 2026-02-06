package com.hust.common.util;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

public class AES256GCMUtil {
    private static final String AES = "AES";
    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int KEY_SIZE_BITS = 256;
    private static final int NONCE_LEN = 16;
    private static final int TAG_LEN_BITS = 128;

    private static final SecureRandom RNG = new SecureRandom();
    private AES256GCMUtil() {}

    public static byte[] generateKey() {
        try {
            KeyGenerator kg = KeyGenerator.getInstance(AES);
            kg.init(KEY_SIZE_BITS, RNG);
            return kg.generateKey().getEncoded();
        } catch (Exception e) {
            throw new RuntimeException("Cannot generate AES-256 key", e);
        }
    }
    public static SecretKey toKey(byte[] keyBytes) {
        if (keyBytes == null || keyBytes.length != 32) {
            throw new IllegalArgumentException("AES-256 key must be 32 bytes");
        }
        return new SecretKeySpec(keyBytes, AES);
    }

    public static String encryptToBase64(String plaintext, byte[] key, byte[] nonce,byte[] aadOptional) {
        byte[] pt = plaintext.getBytes(StandardCharsets.UTF_8);
        byte[] combined = encrypt(pt, key, nonce, aadOptional);
        return Base64.getEncoder().encodeToString(combined);
    }
    public static String decryptFromBase64(String base64Combined, byte[] key, byte[] nonce, byte[] aadOptional) {
        byte[] combined = Base64.getDecoder().decode(base64Combined);
        byte[] pt = decrypt(combined, key, nonce,aadOptional);
        return new String(pt, StandardCharsets.UTF_8);
    }

    public static byte[] encrypt(byte[] plaintext, byte[] keyBytes, byte[] nonce, byte[] aadOptional) {
        try {
            SecretKey key = toKey(keyBytes);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            GCMParameterSpec spec = new GCMParameterSpec(TAG_LEN_BITS, nonce);
            cipher.init(Cipher.ENCRYPT_MODE, key, spec);
            if (aadOptional != null && aadOptional.length > 0) {
                cipher.updateAAD(aadOptional);
            }

            return cipher.doFinal(plaintext);
        } catch (Exception e) {
            throw new RuntimeException("AES-GCM encrypt error", e);
        }
    }

    public static byte[] decrypt(byte[] combined, byte[] keyBytes, byte[] nonce, byte[] aadOptional) {
        if (combined == null || combined.length < NONCE_LEN + 16) {
            throw new IllegalArgumentException("Ciphertext too short");
        }
        try {

            SecretKey key = toKey(keyBytes);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            GCMParameterSpec spec = new GCMParameterSpec(TAG_LEN_BITS, nonce);
            cipher.init(Cipher.DECRYPT_MODE, key, spec);
            if (aadOptional != null && aadOptional.length > 0) {
                cipher.updateAAD(aadOptional);
            }
            return cipher.doFinal(combined);
        } catch (javax.crypto.AEADBadTagException badTag) {
            throw new SecurityException("GCM tag verification failed", badTag);
        } catch (Exception e) {
            throw new RuntimeException("AES-GCM decrypt error", e);
        }
    }

    public static String encryptToBase64(String plaintext, byte[] key, byte[] nonce) {
        return encryptToBase64(plaintext, key, nonce,null);
    }
    public static String decryptFromBase64(String base64Combined, byte[] none, byte[] key) {
        return decryptFromBase64(base64Combined, key, none,null);
    }
}
