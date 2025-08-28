package org.mbg.common.keycloak.repository;

import org.mbg.common.cache.util.CacheConstants;
import org.mbg.common.keycloak.util.KeycloakCacheConstants;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository class acting as an abstraction layer for caching user permissions.
 * <p>
 * This repository leverages Spring's caching annotations ({@link Cacheable} and {@link CachePut})
 * to interact with a configured cache provider. It specifically handles caching operations
 * for user permissions associated with a user ID and client ID, using predefined cache names
 * and key generation expressions from {@link KeycloakCacheConstants}.
 * </p>
 * <p>
 * The methods in this class do not contain actual data fetching logic; instead, they
 * rely on the caching annotations to manage cache reads and writes.
 * </p>
 *
 * @author: LinhLH
 * @see Cacheable
 * @see CachePut
 * @see KeycloakCacheConstants
 */
@Repository
public class PermissionCacheRepository {

    /**
     * Attempts to retrieve a list of permissions for the given user and client ID from the cache.
     * <p>
     * If a cache entry exists for the generated key (based on {@code userId} and {@code clientId}),
     * the cached list is returned directly without executing the method body.
     * If no entry is found, the method body is executed (returning {@code null}), and due to the
     * {@code unless} condition, the {@code null} result is not added to the cache.
     * </p>
     *
     * @param key  the key under which the data should be stored in the cache.
     * @return The cached list of permissions if present in the cache; otherwise, {@code null}.
     */
    @Cacheable(cacheNames = KeycloakCacheConstants.Others.USER_PERMISSION, key = CacheConstants.Expression.KEY,
            unless = CacheConstants.Expression.RESULT_EQUAL_NULL)
    public List<String> getIfPresent(String key) {
        // This method body is only executed if the item is not found in the cache.
        // Returning null ensures that null values are not cached due to the 'unless' condition.
        return null;
    }

    /**
     * Puts or updates the list of permissions for the given user and client ID into the cache.
     * <p>
     * This method always executes its body, returning the provided {@code permissions} list.
     * The {@link CachePut} annotation ensures that this returned list is then placed into the cache,
     * associated with the key generated from {@code userId} and {@code clientId}.
     * The {@code unless} condition prevents caching if the returned list happens to be {@code null}.
     * </p>
     *
     * @param key  the key under which the data should be stored in the cache.
     * @param permissions The list of permission strings to be cached.
     * @return The same list of permissions that was passed in, which will be put into the cache.
     */
    @CachePut(cacheNames = KeycloakCacheConstants.Others.USER_PERMISSION, key = CacheConstants.Expression.KEY,
            unless = CacheConstants.Expression.RESULT_EQUAL_NULL)
    public List<String> put(String key, List<String> permissions) {
        // This method body is always executed.
        // The returned value 'permissions' is placed into the cache by the @CachePut annotation.
        return permissions;
    }
}