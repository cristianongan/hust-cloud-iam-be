package org.mbg.common.base.repository.impl;

import org.mbg.common.base.model.Customer;
import org.mbg.common.base.model.dto.request.LookupReq;
import org.mbg.common.base.repository.extend.CustomerRepositoryExtend;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class CustomerRepositoryImpl implements CustomerRepositoryExtend {
    @Override
    public List<Customer> search(LookupReq lookupReq, Pageable pageable) {
        return List.of();
    }

    @Override
    public Long count(LookupReq lookupReq) {
        return 0L;
    }
}
