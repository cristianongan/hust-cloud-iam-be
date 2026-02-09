package com.hust.common.base.repository;

import com.hust.common.base.model.Record;
import com.hust.common.base.repository.extend.RecordRepositoryExtend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long>, RecordRepositoryExtend {

    Boolean existsByCustomerKeyAndLeakId(String customerKey, String leakId);
}
