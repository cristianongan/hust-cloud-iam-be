package org.mbg.anm.repository.extend;

import org.mbg.anm.model.User;
import org.mbg.anm.model.search.UserSearch;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserRepositoryExtend {
    List<User> search(UserSearch search, Pageable pageable);

    Long count(UserSearch search);
}
