package org.mbg.anm.repository;

import org.mbg.anm.model.User;
import org.mbg.anm.repository.extend.UserRepositoryExtend;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long>, UserRepositoryExtend {

    User findByUsername(String username);
}
