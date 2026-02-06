package com.hust.common.base.repository;

import com.hust.common.base.model.CustomerLog;
import com.hust.common.base.repository.extend.CustomerLogRepositoryExtend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerLogRepository extends JpaRepository<CustomerLog, Long>, CustomerLogRepositoryExtend {
}
