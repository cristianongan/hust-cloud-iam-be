package org.mbg.common.base.repository;

import org.mbg.common.base.constants.CacheConstants;
import org.mbg.common.base.repository.extend.CustomerRepositoryExtend;
import org.mbg.common.base.model.Customer;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long>, CustomerRepositoryExtend {
    @Cacheable(cacheNames = CacheConstants.CUSTOMER.FIND_BY_CUSTOMER_KEY, key = "#customerKey", unless = "#result == null")
    Customer findByCustomerKey(String customerKey);

    @Caching(put = {
            @CachePut(cacheNames = {CacheConstants.CUSTOMER.FIND_BY_CUSTOMER_KEY}, key = "#entity.customerKey",
                    condition = "#entity.customerKey != null"),
    })
    default Customer save_(Customer entity) {
        return save(entity);
    }

    @Caching(evict = {
            @CacheEvict(cacheNames = CacheConstants.CUSTOMER.FIND_BY_CUSTOMER_KEY, allEntries = true, beforeInvocation = true),
            @CacheEvict(cacheNames = CacheConstants.CUSTOMER.FIND_BY_ID_AND_STATUS, allEntries = true, beforeInvocation = true)
    })
    default List<Customer> saveAll_(List<Customer> entities) {
        return saveAll(entities);
    }

    @Query(nativeQuery = true, value = """
    select * from customer where sync_status = :status order by CREATED_DATE limit :limit
        """)
    List<Customer> findBySyncStatusAndLimitOrderCreated(Integer status, int limit);

    @Query(nativeQuery = true, value = """
    select * from customer where sync_status = :status order by last_scan limit :limit
        """)
    List<Customer> findBySyncStatusAndLimitOrderLastSync(Integer status, int limit);

    @Cacheable(cacheNames = CacheConstants.CUSTOMER.FIND_BY_ID_AND_STATUS, unless = "#result == null")
    Customer findByIdAndStatus(Long id, Integer status);

}
