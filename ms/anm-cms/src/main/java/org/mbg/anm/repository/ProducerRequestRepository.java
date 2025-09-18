package org.mbg.anm;

import org.mbg.common.base.model.ProducerRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProducerRequestRepository extends JpaRepository<ProducerRequest,Long> {
}
