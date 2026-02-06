package com.hust.iam.repository;

import com.hust.iam.model.ClientRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRoleRepository extends JpaRepository<ClientRole, Long> {
}
