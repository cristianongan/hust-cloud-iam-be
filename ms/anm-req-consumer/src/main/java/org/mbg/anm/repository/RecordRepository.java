package org.mbg.anm.repository;

import org.mbg.anm.repository.extend.RecordRepositoryExtend;
import org.mbg.common.base.model.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long>, RecordRepositoryExtend {
}
