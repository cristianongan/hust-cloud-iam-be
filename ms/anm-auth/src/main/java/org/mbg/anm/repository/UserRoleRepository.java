package org.mbg.anm.repository;

import org.mbg.anm.model.ClientRole;
import org.mbg.anm.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    @Query(nativeQuery = true, value = "delete from user_role where user_id = :id")
    @Modifying
    Integer removeAllRoleByUserId(Long id);
}
