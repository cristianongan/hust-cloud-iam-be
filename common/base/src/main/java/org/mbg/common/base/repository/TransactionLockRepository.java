package org.mbg.common.base.repository;

import org.mbg.common.base.model.TransactionLock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TransactionLockRepository extends JpaRepository<TransactionLock, String> {
    
    @SuppressWarnings("unchecked")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    TransactionLock saveAndFlush(TransactionLock entity);
    
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void deleteInBatch(Iterable<TransactionLock> entities);
}
