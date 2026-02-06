package com.hust.common.security;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Objects;

@Slf4j
@Getter
@Setter
public class SignatureProvider {

    private RSAPrivateKey privateKey;

    private RSAPublicKey publicKey;

    private Signature sig;

    private SignatureProvider() {

    }

    public static SignatureProvider fromPrivateKey(String keyPem)
                    throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] encoded = Base64.getDecoder().decode(keyPem);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        RSAPrivateKey privateKey = (RSAPrivateKey) kf.generatePrivate(new PKCS8EncodedKeySpec(encoded));

        Signature sig = Signature.getInstance("SHA256WithRSA");

        SignatureProvider sp = new SignatureProvider();

        sp.setSig(sig);
        sp.setPrivateKey(privateKey);

        return sp;
    }

    public String generateSignature(String plainText)
                    throws InvalidKeyException, SignatureException, NoSuchAlgorithmException {
        if (Objects.nonNull(plainText)) {
            this.sig.initSign(this.privateKey);
            this.sig.update(this.hash(plainText).getBytes(StandardCharsets.UTF_8));

            return new String(Base64.getEncoder().encode(sig.sign()), StandardCharsets.UTF_8);
        }

        return null;
    }

    public String hash(String input, String algorithm) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(algorithm);

        digest.reset();
        digest.update(input.getBytes(StandardCharsets.UTF_8));
        
        String rs = String.format("%040x", new BigInteger(1, digest.digest()));

        return rs;

    }

    public String hash(String input) throws NoSuchAlgorithmException {
        return this.hash(input, "SHA-1");
    }
}
