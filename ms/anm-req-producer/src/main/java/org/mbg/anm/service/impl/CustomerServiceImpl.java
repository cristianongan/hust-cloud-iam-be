package org.mbg.anm.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mbg.anm.model.dto.request.SubscribeReq;
import org.mbg.anm.model.dto.response.SubscribeRes;
import org.mbg.anm.repository.CustomerDataRepository;
import org.mbg.anm.repository.CustomerRepository;
import org.mbg.anm.service.CustomerService;
import org.mbg.common.api.enums.ClientResponseError;
import org.mbg.common.api.exception.ClientResponseException;
import org.mbg.common.base.model.Customer;
import org.mbg.common.security.util.SecurityUtils;
import org.mbg.common.util.Validator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    private final CustomerDataRepository customerDataRepository;

    @Override
    @Transactional
    public SubscribeRes subscribe(SubscribeReq subscribeReq) {
        final String clientId = SecurityUtils.getCurrentUserLogin().orElse(null);

        if (Validator.isNull(clientId)) {
            throw new ClientResponseException(ClientResponseError.UNAUTHORIZED);
        }

        if (Validator.isNull(subscribeReq.getSubscriberId())) {
            throw new ClientResponseException(ClientResponseError.INVALID_SUBSCRIBER_ID);
        }

        final String key = this.getKey(clientId, subscribeReq.getSubscriberId());

//        Customer customer =

        return null;
    }

    private String getKey(String clientId, String key) {
        return clientId + "_" + key;
    }
}
