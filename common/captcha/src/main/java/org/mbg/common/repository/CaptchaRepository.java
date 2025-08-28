package org.mbg.common.repository;

import org.mbg.common.cache.util.CacheConstants;
import org.mbg.common.util.CaptchaCacheConstants;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

/**
 * Repository class for managing cached CAPTCHA data.
 * <p>
 * Provides methods to store, retrieve, and invalidate CAPTCHA entries within the cache.
 * <p>
 * Caching operations utilize Spring's cache annotations, such as {@code @CachePut},
 * {@code @Cacheable}, and {@code @CacheEvict}, with specific configurations to manage the
 * {@code CaptchaCacheConstants.Others.CAPTCHA} namespace.
 *
 * <p>
 * These operations are useful for entities or processes that rely on CAPTCHA caching
 * for validation purposes, ensuring scalability and performance efficiency.
 *
 * @author LinhLH
 * @since 19/04/2025
 */
@Repository
public class CaptchaRepository {

	/**
	 * Stores the provided data in the cache with the specified key.
	 * <p>
	 * Utilizes {@code @CachePut} to ensure the cache is updated regardless of the method execution outcome.
	 * The cache entry is placed within the {@code CaptchaCacheConstants.Others.CAPTCHA} namespace
	 * and managed using the key provided. The condition for caching avoids storing null results.
	 *
	 * @param key the key under which the data should be stored in the cache.
	 * @param data the data to be stored in the cache.
	 * @return the provided data as confirmation of the caching operation.
	 */
	@CachePut(cacheNames = CaptchaCacheConstants.Others.CAPTCHA, key = CacheConstants.Expression.KEY,
			unless = CacheConstants.Expression.RESULT_EQUAL_NULL)
	public String put(String key, String data) {
		return data;
	}

	/**
	 * Retrieves the cached CAPTCHA value associated with the provided key, if it exists.
	 * <p>
	 * This method checks the cache for the specified key and retrieves the corresponding value
	 * from the {@code CaptchaCacheConstants.Others.CAPTCHA} cache namespace.
	 * The method uses caching behavior defined by the {@code @Cacheable} annotation,
	 * avoiding the storage or retrieval of null results as specified by the {@code unless} condition.
	 *
	 * @param key the key associated with the CAPTCHA data in the cache.
	 * @return the CAPTCHA value associated with the specified key, or {@code null} if no data is present in the cache.
	 */
	@Cacheable(cacheNames = CaptchaCacheConstants.Others.CAPTCHA, key = CacheConstants.Expression.KEY,
			unless = CacheConstants.Expression.RESULT_EQUAL_NULL)
	public String getIfPresent(String key) {
		return null;
	}

	/**
	 * Invalidates the CAPTCHA cache entry associated with the given key.
	 * <p>
	 * This method removes a specific cache entry from the {@code AnnotationCacheConstants.Others.CAPTCHA} namespace
	 * using the provided key. It leverages the {@code @CacheEvict} annotation to perform the invalidation operation.
	 *
	 * @param key the key associated with the CAPTCHA cache entry to be invalidated.
	 * @return the provided key as confirmation of the invalidation operation.
	 */
	@CacheEvict(cacheNames = CaptchaCacheConstants.Others.CAPTCHA, key = CacheConstants.Expression.KEY)
	public String invalidate(String key) {
		return key;
	}

}
