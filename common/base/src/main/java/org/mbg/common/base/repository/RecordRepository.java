package org.mbg.common.base.repository;

import org.mbg.common.base.model.Record;
import org.mbg.common.base.repository.extend.RecordRepositoryExtend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long>, RecordRepositoryExtend {
}
