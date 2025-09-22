package org.mbg.anm.repository;

import org.mbg.anm.repository.extend.ProducerRequestRepositoryExtend;
import org.mbg.common.base.model.ProducerRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProducerRequestRepository extends JpaRepository<ProducerRequest,Long>, ProducerRequestRepositoryExtend {
}
