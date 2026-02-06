package com.hust.iam.repository;

import org.mbg.common.base.model.ProducerRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProducerRequestRepository extends JpaRepository<ProducerRequest,Long> {

    @Query(nativeQuery = true, value = "select * from rp_request where status = :status limit :limit")
    List<ProducerRequest> findByStatusLimit(int status, int limit);
}
