package com.hust.common.base.repository;

import com.hust.common.base.constants.CacheConstants;
import com.hust.common.base.repository.extend.CustomerRepositoryExtend;
import com.hust.common.base.model.Customer;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long>, CustomerRepositoryExtend {
    @Cacheable(cacheNames = CacheConstants.CUSTOMER.FIND_BY_CUSTOMER_KEY, key = "#customerKey", unless = "#result == null")
    Customer findByCustomerKeyAndStatusNot(String customerKey, Integer status);

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

    @Cacheable(cacheNames = CacheConstants.CUSTOMER.FIND_BY_ID_AND_STATUS, unless = "#result == null")
    Customer findByIdAndStatus(Long id, Integer status);


    @Cacheable(cacheNames = CacheConstants.CUSTOMER.FIND_USER_ID, key = "#userId", unless = "#result == null")
    Customer findByUserIdAndStatusNot(Long userId, Integer status);

    List<Customer> findByCustomerKeyInAndStatusNot(List<String> customerIds, Integer status);

    @Modifying
    @Query(nativeQuery = true, value = """
    update customer set status = :status where end_time <= :now
        """)
    void updateStatusExpiredCustomer(Integer status, LocalDateTime now);

}
