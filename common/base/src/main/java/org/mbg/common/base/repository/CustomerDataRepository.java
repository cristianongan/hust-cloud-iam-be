package org.mbg.common.base.repository;

import org.mbg.common.base.model.CustomerData;
import org.mbg.common.base.repository.extend.CustomerDataRepositoryExtend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerDataRepository extends JpaRepository<CustomerData,Long>, CustomerDataRepositoryExtend {
    List<CustomerData> findByCustomerKeyAndStatus(String customerKey, Integer status);

    CustomerData findByIdAndStatus(Long id, Integer status);

    CustomerData findByCustomerKeyAndValueAndStatus(String customerKey, String value, Integer status);

    List<CustomerData> findByCustomerKeyAndStatusAndValueIn(String customerKey, Integer status, List<String> values);
}
