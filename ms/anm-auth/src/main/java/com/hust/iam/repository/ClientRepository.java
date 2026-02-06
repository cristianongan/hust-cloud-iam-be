package com.hust.iam.repository;

import com.hust.iam.model.Client;
import com.hust.iam.repository.extend.ClientRepositoryExtend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long>, ClientRepositoryExtend {
    Client findByClientIdAndStatus(String clientId, Integer status);

    Client findByClientId(String clientId);

    @Query(nativeQuery = true, value = """
    update client_ set status = :status where client_id in :ids
        """)
    @Modifying
    void updateStatusByClientIds(List<String> ids, Integer status);

    @Query(nativeQuery = true, value = """
    update client_ set status = :status where user_id in :ids
        """)
    @Modifying
    void updateStatusByUserIds(List<Long> ids, Integer status);
}
