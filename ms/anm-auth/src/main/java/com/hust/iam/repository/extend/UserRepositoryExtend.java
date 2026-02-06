package com.hust.iam.repository.extend;

import com.hust.iam.model.User;
import com.hust.iam.model.search.UserSearch;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserRepositoryExtend {
    List<User> search(UserSearch search, Pageable pageable);

    Long count(UserSearch search);
}
