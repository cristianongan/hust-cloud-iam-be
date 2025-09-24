package org.mbg.anm.repository;

import org.mbg.anm.model.User;
import org.mbg.anm.model.search.UserSearch;
import org.mbg.anm.repository.extend.UserRepositoryExtend;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Long>, UserRepositoryExtend {

    User findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmailAndStatusNot(String email, Integer status);

    Boolean existsByPhoneAndStatusNot(String phone, Integer status);

    @Query("update User u set u.status = :status where u.id in :ids")
    @Modifying
    Integer updateStatusByIdIn(int status, Collection<Long> ids);

    Boolean existsByIdAndStatus(Long id, Integer status);

    Boolean existsByUsernameAndStatus(String username, Integer status);
}
