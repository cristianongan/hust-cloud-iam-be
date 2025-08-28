package org.mbg.common.keycloak.service.impl;

import org.mbg.common.keycloak.repository.PermissionCacheRepository;
import org.mbg.common.keycloak.service.PermissionCacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Default implementation of the {@link PermissionCacheService}.
 * <p>
 * This service is intended to provide a caching layer for user permissions
 * associated with specific client IDs to reduce direct calls to Keycloak.
 * Currently, this implementation serves as a placeholder (TODO) and does not
 * perform any actual caching. It returns empty lists and performs no caching operations.
 * </p>
 * <p>
 * A proper implementation would typically involve integrating with a caching solution
 * like Redis, Caffeine, Hazelcast, or Spring Cache abstraction.
 * </p>
 *
 * @author: LinhLH
 * @see PermissionCacheService
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class PermissionCacheServiceImpl implements PermissionCacheService {

    private final PermissionCacheRepository permissionCacheRepository;
    /**
     * Retrieves the cached permissions for a given user and client ID.
     * <p>
     * <strong>Note:</strong> This is currently a placeholder implementation (TODO)
     * and always returns an empty list. A real implementation would query the
     * configured cache.
     * </p>
     *
     * @param key  the key under which the data should be stored in the cache.
     * @return A list of permission strings (typically roles) from the cache, or {@code null}
     *         or an empty list if not found or not implemented. Currently returns an empty list.
     */
    @Override
    public List<String> getPermissions(String key) {
        return permissionCacheRepository.getIfPresent(key);
    }

    /**
     * Caches the provided list of permissions for a given user and client ID with a specified time-to-live.
     * <p>
     * <strong>Note:</strong> This is currently a placeholder implementation (TODO)
     * and does not perform any caching operations. A real implementation would store
     * the permissions in the configured cache with the given expiration.
     * </p>
     *
     * @param key  the key under which the data should be stored in the cache.
     * @param permissions The list of permission strings (roles) to cache.
     */
    @Override
    public void cachePermissions(String key, List<String> permissions) {
        permissionCacheRepository.put(key, permissions);
    }
}