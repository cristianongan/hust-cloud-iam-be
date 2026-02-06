package com.hust.common.base.repository;

import com.hust.common.base.constants.CacheConstants;
import com.hust.common.base.model.Quota;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuotaRepository extends JpaRepository<Quota, Long> {

    @Cacheable(cacheNames = CacheConstants.QUOTA.FIND_BY_USER_ID, key = "#userId", unless = "#result == null")
    Quota findByUserId(Long userId);

    @Caching(put = {
            @CachePut(cacheNames = {CacheConstants.QUOTA.FIND_BY_USER_ID}, key = "#entity.userId",
                    condition = "#entity.userId != null"),
    })
    default Quota save_(Quota entity) {
        return save(entity);
    }

    @Query(nativeQuery = true, value = """
        update table quota set in_use = in_use+1
            where user_id = :userId
            and is_use <= total
        returning in_use
    """)
    @Modifying
    @Caching(evict = {
            @CacheEvict(cacheNames = {CacheConstants.QUOTA.FIND_BY_USER_ID}, key = "#userId"),
    })
    Long increaseByUserId(Long userId);

    List<Quota> findByUserIdIn(List<Long> userIds);
}
