package org.mbg.common.base.repository;

import org.mbg.common.base.model.CustomerData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerDataRepository extends JpaRepository<CustomerData,Long> {
    List<CustomerData> findByCustomerId(Long customerId);
}
