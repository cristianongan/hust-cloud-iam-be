package com.hust.common.base.repository;

import com.hust.common.base.model.Record;
import com.hust.common.base.model.RecordType;
import com.hust.common.base.repository.extend.RecordRepositoryExtend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long>, RecordRepositoryExtend {

    Boolean existsByCustomerKeyAndLeakId(String customerKey, String leakId);
}
