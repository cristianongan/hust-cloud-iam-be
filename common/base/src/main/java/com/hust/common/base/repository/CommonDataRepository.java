package com.hust.common.base.repository;

import com.hust.common.base.constants.CacheConstants;
import com.hust.common.base.model.CommonData;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommonDataRepository extends JpaRepository<CommonData, Long> {
    @Cacheable(cacheNames = CacheConstants.COMMON_DATA.FIND_BY_TYPE_AND_STATUS,
            key = "{#type, #status}",
            unless = "#result == null")
    List<CommonData> findByTypeAndStatus(String type, Integer status);
}
