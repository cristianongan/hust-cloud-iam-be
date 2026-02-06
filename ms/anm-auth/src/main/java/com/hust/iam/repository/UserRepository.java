package com.hust.iam.repository;

import com.hust.iam.model.User;
import com.hust.iam.repository.extend.UserRepositoryExtend;
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

    List<User> findByIdIn(List<Long> ids);

    User findByPhoneAndStatusNot(String phone, Integer status);

    User findByEmailAndStatusNot(String email, Integer status);

    @Query(nativeQuery = true, value = """
    select e.* from user_ e inner join client_ c on c.user_id = e.id
        where c.client_id = :clientId
        limit 1
        """)
    User findByClientId(String clientId);
}
