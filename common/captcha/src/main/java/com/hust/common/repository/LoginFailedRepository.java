/**
 * 
 */
package com.hust.common.repository;

import com.hust.common.cache.util.CacheConstants;
import com.hust.common.util.CaptchaCacheConstants;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

/**
 * Interface representing operations for managing login failure counts.
 * <p>
 * Defines methods to put, retrieve, and invalidate cache entries associated with login failures.
 *
 * @author LinhLH
 * @since 19/04/2025
 */
@Repository
public class LoginFailedRepository {

	/**
	 * Interface representing operations for managing login failure counts.
	 * <p>
	 * Defines methods to put, retrieve, and invalidate cache entries associated with login failures.
	 *
	 * @author LinhLH
	 * @since 19/04/2025
	 */
	@CachePut(cacheNames = CaptchaCacheConstants.Others.LOGIN_FAILED, key = CacheConstants.Expression.KEY,
			unless = CacheConstants.Expression.RESULT_EQUAL_NULL)
	public Integer put(String key, Integer value) {
		return value;
	}

	/**
	 * Retrieves the cached login failure count associated with the provided key if it exists.
	 * <p>
	 * This method checks the cache for the given key and returns the corresponding value
	 * if it is present. The value is retrieved from the cache identified by the
	 * {@code AnnotationCacheConstants.Others.LOGIN_FAILED} name.
	 *
	 * @param key the key associated with the login failure count in the cache.
	 * @return the login failure count associated with the specified key, or {@code null} if no data is present in the cache.
	 */
	@Cacheable(cacheNames = CaptchaCacheConstants.Others.LOGIN_FAILED, key = CacheConstants.Expression.KEY,
			unless = CacheConstants.Expression.RESULT_EQUAL_NULL)
	public Integer getIfPresent(String key) {
		return null;
	}

	/**
	 * Removes the cached entry associated with the provided key.
	 * <p>
	 * This method invalidates a cache entry within the {@code AnnotationCacheConstants.Others.LOGIN_FAILED}
	 * cache by removing the corresponding data based on the provided key.
	 *
	 * @param key the key associated with the cache entry to be invalidated.
	 * @return the provided key as confirmation of the invalidation operation.
	 */
	@CacheEvict(cacheNames = CaptchaCacheConstants.Others.LOGIN_FAILED, key = CacheConstants.Expression.KEY)
	public String invalidate(String key) {
		return key;
	}
}
