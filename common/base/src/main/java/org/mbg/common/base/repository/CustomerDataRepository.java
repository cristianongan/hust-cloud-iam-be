package org.mbg.common.base.repository;

import org.mbg.common.base.model.CustomerData;
import org.mbg.common.base.repository.extend.CustomerDataRepositoryExtend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerDataRepository extends JpaRepository<CustomerData,Long>, CustomerDataRepositoryExtend {
    List<CustomerData> findByCustomerIdAndStatus(Long customerId, Integer status);

    CustomerData findByIdAndStatus(Long id, Integer status);

    CustomerData findByCustomerIdAndValueAndStatus(Long customerId, String value, Integer status);

    List<CustomerData> findByCustomerIdAndStatusAndValueIn(Long customerId, Integer status, List<String> values);
}
