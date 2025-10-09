package org.mbg.common.base.repository;

import org.mbg.common.base.model.Record;
import org.mbg.common.base.model.RecordType;
import org.mbg.common.base.repository.extend.RecordRepositoryExtend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long>, RecordRepositoryExtend {

    Boolean existsByCustomerIdAndLeakId(Long customerId, String leakId);
}
