/**
 * 
 */
package com.hust.repository;

import java.util.UUID;

import com.hust.common.util.StringPool;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import com.hust.cache.util.OtpCacheConstants;
import com.hust.model.OtpValue;

public interface OtpRepository {

	/**
	 * @param key
	 * @param otp
	 */
	@CachePut(cacheNames = OtpCacheConstants.Others.OTP, key = "#key", unless = "#result == null")
	default OtpValue put(String key, OtpValue otp) {
		return otp;
	}

	/**
	 * @param key
	 * @return
	 */
	@Cacheable(cacheNames = OtpCacheConstants.Others.OTP, key = "#key", unless = "#result == null")
	default OtpValue getIfPresent(String key) {
		return null;
	}

	/**
	 * @param key
	 */
	@CacheEvict(cacheNames = OtpCacheConstants.Others.OTP, key = "#key")
	default String invalidate(String key) {
		return key;
	}

	/**
	 * 
	 * @param phoneNumber
	 * @return
	 */
	@CachePut(cacheNames = OtpCacheConstants.Others.OTP_VERIFY, key = "#phoneNumber", unless = "#result == null")
	default String putTransaction(String phoneNumber) {
		return UUID.randomUUID().toString();
	}

	@CachePut(cacheNames = OtpCacheConstants.Others.OTP_VERIFY, key = "#phoneNumber", unless = "#result == null")
    default String putTransaction(String phoneNumber, String extend) {
        StringBuilder sb = new StringBuilder(3);
	    
        sb.append(UUID.randomUUID());
        sb.append(StringPool.COLON);
        sb.append(extend);
        
	    return sb.toString();
    }
	
	/**
	 * get transaction by phone number
	 * @param phoneNumber
	 * @return
	 */
	@Cacheable(cacheNames = OtpCacheConstants.Others.OTP_VERIFY, key = "#phoneNumber", unless = "#result == null")
	default String getTransactionIfPresent(String phoneNumber) {
		return null;
	}

	/**
	 * @param phoneNumber
	 */
	@CacheEvict(cacheNames = OtpCacheConstants.Others.OTP_VERIFY, key = "#phoneNumber")
	default String invalidateTransaction(String phoneNumber) {
		return phoneNumber;
	}
}
