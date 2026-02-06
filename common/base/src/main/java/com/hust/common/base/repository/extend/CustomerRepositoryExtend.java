package com.hust.common.base.repository.extend;

import com.hust.common.base.model.Customer;
import com.hust.common.base.model.dto.request.LookupReq;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomerRepositoryExtend {
    List<Customer> search(LookupReq lookupReq, Pageable pageable);

    Long count(LookupReq lookupReq);
}
