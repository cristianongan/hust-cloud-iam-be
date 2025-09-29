package org.mbg.anm.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mbg.anm.model.dto.request.CustomerDataReq;
import org.mbg.anm.model.dto.response.RecordResponse;
import org.mbg.anm.service.mapper.RecordMapper;
import org.mbg.common.base.model.Record;
import org.mbg.common.base.model.dto.request.LookupReq;
import org.mbg.anm.model.dto.request.SubscribeReq;
import org.mbg.anm.model.dto.response.LookupResponse;
import org.mbg.anm.model.dto.response.SubscribeRes;
import org.mbg.common.base.enums.CustomerSyncStatus;
import org.mbg.common.base.repository.CustomerDataRepository;
import org.mbg.common.base.repository.CustomerRepository;
import org.mbg.anm.service.CustomerService;
import org.mbg.anm.service.mapper.CustomerMapper;
import org.mbg.common.api.enums.ClientResponseError;
import org.mbg.common.api.exception.ClientResponseException;
import org.mbg.common.base.enums.CustomerDataType;
import org.mbg.common.base.enums.EntityStatus;
import org.mbg.common.base.model.Customer;
import org.mbg.common.base.model.CustomerData;
import org.mbg.common.base.repository.RecordRepository;
import org.mbg.common.security.util.SecurityUtils;
import org.mbg.common.util.Validator;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    private final CustomerDataRepository customerDataRepository;

    private final CustomerMapper customerMapper;

    private final RecordRepository recordRepository;

    private final RecordMapper recordMapper;

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

        Customer customer = this.customerRepository.findByCustomerKey(key);

        if (Validator.isNull(customer)) {
            customer = new Customer();
            customer.setCustomerKey(key);
            customer.setSubscriberId(subscribeReq.getSubscriberId());
            customer.setClientId(clientId);

        }

        customer.setReference(subscribeReq.getReference());
        customer.setStatus(EntityStatus.ACTIVE.getStatus());
        customer.setSyncStatus(CustomerSyncStatus.NEW.getStatus());
        customer.setWaitAtLease(LocalDateTime.now());

        customer = this.customerRepository.save_(customer);

        List<CustomerData> datas = new ArrayList<>();
        if (Validator.isNotNull(subscribeReq.getDataReqs())) {
            for (CustomerDataReq item : subscribeReq.getDataReqs()) {
                if (Validator.isNotNull(item)) {
                    CustomerData data = new CustomerData();
                    data.setCustomerId(customer.getId());
                    CustomerDataType type = CustomerDataType.resolveByName(item.getType());
                    if (Validator.isNull(type) || Validator.isNull(type.getValue())) {
                        throw new ClientResponseException(ClientResponseError.INVALID_DATA_TYPE);
                    }
                    data.setType(type.getValue());
                    data.setValue(item.getValue());

                    datas.add(data);
                }
            }
        }

        this.customerDataRepository.saveAll(datas);

        return SubscribeRes.builder().subscriberId(customer.getSubscriberId()).build();
    }

    @Override
    public SubscribeRes unSubscribe(SubscribeReq subscribeReq) {
        final String clientId = SecurityUtils.getCurrentUserLogin().orElse(null);

        if (Validator.isNull(clientId)) {
            throw new ClientResponseException(ClientResponseError.UNAUTHORIZED);
        }

        final String key = this.getKey(clientId, subscribeReq.getSubscriberId());

        Customer customer = this.customerRepository.findByCustomerKey(key);

        if (Validator.isNull(customer)) {
            throw new ClientResponseException(ClientResponseError.INVALID_SUBSCRIBER_ID);
        }

        customer.setStatus(EntityStatus.INACTIVE.getStatus());
        customer.setSyncStatus(CustomerSyncStatus.CLOSED.getStatus());
        this.customerRepository.save_(customer);

        return SubscribeRes.builder().subscriberId(customer.getSubscriberId()).build();
    }

    @Override
    public LookupResponse lookup(LookupReq lookupReq) {
        if (Validator.isNull(lookupReq.getSubscriberId())) {
            throw new ClientResponseException(ClientResponseError.INVALID_SUBSCRIBER_ID);
        }

        Pageable pageable = PageRequest.of(lookupReq.getPage(), lookupReq.getPageSize());

        List<Record> records = this.recordRepository.search(lookupReq, pageable);

        List<RecordResponse> content = this.recordMapper.toDto(records);

        Long count  = this.recordRepository.count(lookupReq);

        return LookupResponse.builder()
                .page(new PageImpl<>(content, pageable, count))
                .subscriberId(lookupReq.getSubscriberId())
                .build();
    }

    private String getKey(String clientId, String key) {
        return clientId + "_" + key;
    }
}
