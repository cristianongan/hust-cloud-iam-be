package org.mbg.anm.repository;

import org.mbg.anm.model.Role;
import org.mbg.anm.repository.extend.RoleRepositoryExtend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long>, RoleRepositoryExtend {
    boolean existsByCodeAndStatusNot(String code, int status);

    Role findByCodeAndStatusNot(String code, int status);

    @Query("update Role u set u.status = :status where u.id in :ids")
    @Modifying
    Integer updateStatusByIdIn(int status, Collection<Long> ids);

    List<Role> findByCodeIn(List<String> roleCode);

    List<Role> findAllByStatus(Integer status);
}
