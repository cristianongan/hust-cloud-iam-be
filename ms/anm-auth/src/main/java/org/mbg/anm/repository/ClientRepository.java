package org.mbg.anm.repository;

import org.mbg.anm.model.Client;
import org.mbg.anm.model.dto.ClientDTO;
import org.mbg.anm.repository.extend.ClientRepositoryExtend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long>, ClientRepositoryExtend {
    Client findByClientId(String clientId);
}
