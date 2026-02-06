package com.hust.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * 
 * @author LinhLH
 *
 */

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HMACUtil {

	/** The Constant HMACSHA256. */
	public static final String HMACSHA256 = "HmacSHA256";

	/** The Constant SHA256. */
	public static final String SHA256 = "SHA-256";

	/**
	 * Encode base 64.
	 *
	 * @param messages  the messages
	 * @param separator  the separator
	 * @param keyString the key string
	 * @param algo      the algo
	 * @return the string
	 */
	public static String encodeBase64(Object[] messages, String separator, String keyString, String algo) {
		String joinMessage = StringUtil.join(messages, separator);

		return encodeBase64(joinMessage, keyString, algo);
	}

	/**
	 * Encode base 64.
	 *
	 * @param message   the message
	 * @param keyString the key string
	 * @param algo      the algo
	 * @return the string
	 */
	public static String encodeBase64(String message, String keyString, String algo) {
		try {
			Mac hashed = Mac.getInstance(algo);

			hashed.init(new SecretKeySpec(keyString.getBytes(), algo));

			byte[] hash = hashed.doFinal(message.getBytes());

			return Base64.getEncoder().encodeToString(hash);
		} catch (InvalidKeyException | NoSuchAlgorithmException e) {
			_log.error(e.getMessage(), e);
		}

		return null;
	}

	/**
	 * Encode hex.
	 *
	 * @param messages  the messages
	 * @param separator  the separator
	 * @param keyString the key string
	 * @param algo      the algo
	 * @return the string
	 */
	public static String encodeHex(Object[] messages, String separator, String keyString, String algo) {
		String joinMessage = StringUtil.join(messages, separator);

		return encodeHex(joinMessage, keyString, algo);
	}

	/**
	 * Encode hex.
	 *
	 * @param message   the message
	 * @param keyString the key string
	 * @param algo      the algo
	 * @return the string
	 */
	public static String encodeHex(String message, String keyString, String algo) {
		byte[] hashBytes = hashToBytes(message, keyString, algo);

		return hashBytes.length == 0 ? null : Hex.encodeHexString(hashBytes);
	}

	public static String encodeHex(byte[] dataInBytes) {
		return dataInBytes.length == 0 ? null : Hex.encodeHexString(dataInBytes);
	}

	/**
	 * Hash.
	 *
	 * @param messages the messages
	 * @param separator the separator
	 * @param algo     the algo
	 * @return the string
	 */
	public static String hash(Object[] messages, String separator, String algo) {
		String joinMessage = StringUtil.join(messages, separator);

		return hash(joinMessage, algo);
	}

	/**
	 * Hash.
	 *
	 * @param joinMessage the join message
	 * @param algo        the algo
	 * @return the string
	 */
	public static String hash(String joinMessage, String algo) {
		StringBuilder sb = new StringBuilder();

		try {
			MessageDigest mDigest = MessageDigest.getInstance(algo);

			byte[] result = mDigest.digest(joinMessage.getBytes());

			for (byte b : result) {
				sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
			}

		} catch (NoSuchAlgorithmException e) {
			_log.error(e.getMessage(), e);
		}

		return sb.toString();
	}

	public static byte[] hashToBytes(String message, String keyString, String algo) {
		try {
			Mac hashed = Mac.getInstance(algo);

			hashed.init(new SecretKeySpec(keyString.getBytes(), algo));

			return hashed.doFinal(message.getBytes());
		} catch (InvalidKeyException | NoSuchAlgorithmException e) {
			_log.error(e.getMessage(), e);
		}

		return new byte[0];
	}

	public static byte[] decodeHex(String data) {
        try {
            return Hex.decodeHex(data.toCharArray());
        } catch (DecoderException e) {
			_log.error(e.getMessage(), e);
        }

		return new byte[0];
    }

	/**
	 * Hash.
	 *
	 * @param message   the message
	 * @param keyString the key string
	 * @param algo      the algo
	 * @return the string
	 */
	public static String hash(String message, String keyString, String algo) {
		byte[] hashBytes = hashToBytes(message, keyString, algo);

		return hashBytes.length == 0 ? null : new String(hashBytes);
	}

	public static String hashSha256(String message) {
		return hash(message, SHA256);
	}

	/**
	 * Sha 256 hex.
	 *
	 * @param messages the messages
	 * @param separator the separator
	 * @return the string
	 */
	public static String sha256Hex(Object[] messages, String separator) {
		String joinMessage = StringUtil.join(messages, separator);

		return DigestUtils.sha256Hex(joinMessage);
	}

	public static boolean isSignatureValid(byte[] signatureInBytes, byte[] dataInBytes) {
		return MessageDigest.isEqual(signatureInBytes, dataInBytes);
	}
}
