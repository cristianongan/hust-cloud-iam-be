package org.mbg.common.keycloak.service;

import java.util.List;

/**
 * @author: LinhLH
 **/
public interface PermissionCacheService {
    List<String> getPermissions(String key);

    void cachePermissions(String key, List<String> permissions);
}
