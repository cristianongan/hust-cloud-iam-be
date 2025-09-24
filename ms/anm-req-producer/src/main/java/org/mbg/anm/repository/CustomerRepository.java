package org.mbg.anm.repository;

import org.mbg.anm.constant.ReqProducerConstant;
import org.mbg.anm.repository.extend.CustomerRepositoryExtend;
import org.mbg.common.base.model.Customer;
import org.mbg.common.security.util.SecurityConstants;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long>, CustomerRepositoryExtend {
    @Cacheable(cacheNames = ReqProducerConstant.CACHE.CUSTOMER_KEY, key = "#customerKey", unless = "#result == null")
    Customer findByCustomerKey(String customerKey);

    @Caching(put = {
            @CachePut(cacheNames = {ReqProducerConstant.CACHE.CUSTOMER_KEY}, key = "#entity.customerKey",
                    condition = "#entity.customerKey != null"),
//            @CachePut(cacheNames = {BusinessCacheConstants.Customer.FIND_BY_PHONE_NUMBER},
//                    key = "#entity.phoneNumber"),
//            @CachePut(cacheNames = {BusinessCacheConstants.Customer.FIND_BY_ID}, key = "#result.customerId")
    })
    default Customer save_(Customer entity) {
        return save(entity);
    }

}
