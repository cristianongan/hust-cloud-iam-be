package org.mbg.common.base.repository;

import org.mbg.common.base.model.CustomerLog;
import org.mbg.common.base.repository.extend.CustomerLogRepositoryExtend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerLogRepository extends JpaRepository<CustomerLog, Long>, CustomerLogRepositoryExtend {
}
