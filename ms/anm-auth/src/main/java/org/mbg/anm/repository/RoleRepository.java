package org.mbg.anm.repository;

import org.mbg.anm.model.Role;
import org.mbg.anm.repository.extend.RoleRepositoryExtend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long>, RoleRepositoryExtend {

}
