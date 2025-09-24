package org.mbg.anm.repository;

import org.mbg.anm.repository.extend.CustomerRepositoryExtend;
import org.mbg.common.base.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long>, CustomerRepositoryExtend {


}
