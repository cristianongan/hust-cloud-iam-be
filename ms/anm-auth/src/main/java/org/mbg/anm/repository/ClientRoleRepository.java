package org.mbg.anm.repository;

import org.mbg.anm.model.ClientRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRoleRepository extends JpaRepository<ClientRole, Long> {
}
