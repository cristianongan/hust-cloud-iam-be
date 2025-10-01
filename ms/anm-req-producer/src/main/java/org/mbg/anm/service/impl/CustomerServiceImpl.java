package org.mbg.anm.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mbg.anm.model.dto.request.CustomerDataReq;
import org.mbg.anm.model.dto.response.InfoRes;
import org.mbg.anm.model.dto.response.RecordResponse;
import org.mbg.anm.service.mapper.RecordMapper;
import org.mbg.common.api.exception.BadRequestException;
import org.mbg.common.base.enums.ErrorCode;
import org.mbg.common.base.model.Record;
import org.mbg.common.base.model.dto.request.LookupReq;
import org.mbg.anm.model.dto.request.SubscribeReq;
import org.mbg.anm.model.dto.response.LookupResponse;
import org.mbg.anm.model.dto.response.SubscribeRes;
import org.mbg.common.base.enums.CustomerSyncStatus;
import org.mbg.common.base.model.dto.request.OtpReq;
import org.mbg.common.base.model.dto.response.TransactionResponse;
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
import org.mbg.enums.OtpType;
import org.mbg.service.OtpService;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.parser.Entity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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

    private final OtpService otpService;

    @Override
    @Transactional
    public SubscribeRes subscribe(SubscribeReq subscribeReq) {
        final String clientId = SecurityUtils.getCurrentUserLogin().orElse(null);

        if (Validator.isNull(clientId)) {
            throw new BadRequestException(ErrorCode.MSG1028);
        }

        if (Validator.isNull(subscribeReq.getSubscriberId())) {
            throw new BadRequestException(ErrorCode.MSG1027);
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

        List<CustomerData> olds = this.customerDataRepository.findByCustomerIdAndStatus(customer.getId(), EntityStatus.ACTIVE.getStatus());

        Map<String, CustomerData> oldMap = olds.stream().collect(Collectors.toMap(CustomerData::getValue, r -> r));

        List<CustomerData> datas = new ArrayList<>();
        if (Validator.isNotNull(subscribeReq.getDataReqs())) {
            for (CustomerDataReq item : subscribeReq.getDataReqs()) {
                CustomerData data = oldMap.getOrDefault(item.getValue(), null);

                if (Validator.isNull(item)) {
                    throw new BadRequestException(ErrorCode.MSG1030);
                }

                if (Validator.isNull(item.getStatus())) {
                    item.setStatus(EntityStatus.ACTIVE.getStatus());
                } else if (!Validator.equals(item.getStatus(), EntityStatus.DELETED.getStatus())
                        && !Validator.equals(item.getStatus(), EntityStatus.ACTIVE.getStatus())) {
                    throw new BadRequestException(ErrorCode.MSG1031);
                }

                if (Validator.isNull(data)) {
                    data = new CustomerData();
                    data.setCustomerId(customer.getId());
                    CustomerDataType type = CustomerDataType.resolveByName(item.getType());
                    if (Validator.isNull(type) || Validator.isNull(type.getValue())) {
                        throw new BadRequestException(ErrorCode.MSG1030);
                    }
                    data.setType(type.getValue());
                    data.setValue(item.getValue());

                    datas.add(data);
                } else {
                    data.setStatus(item.getStatus());
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
            throw new BadRequestException(ErrorCode.MSG1028);
        }

        final String key = this.getKey(clientId, subscribeReq.getSubscriberId());

        Customer customer = this.customerRepository.findByCustomerKey(key);

        if (Validator.isNull(customer)) {
            throw new BadRequestException(ErrorCode.MSG1027);
        }

        customer.setStatus(EntityStatus.INACTIVE.getStatus());
        customer.setSyncStatus(CustomerSyncStatus.CLOSED.getStatus());
        this.customerRepository.save_(customer);

        return SubscribeRes.builder().subscriberId(customer.getSubscriberId()).build();
    }

    @Override
    public LookupResponse lookup(LookupReq lookupReq) {
        if (Validator.isNull(lookupReq.getSubscriberId())) {
            throw new BadRequestException(ErrorCode.MSG1027);
        }

        List<Record> records = this.recordRepository.search(lookupReq);

        List<RecordResponse> content = this.recordMapper.toDto(records);

        return LookupResponse.builder()
                .data(content)
                .subscriberId(lookupReq.getSubscriberId())
                .build();
    }

    @Override
    public InfoRes info(SubscribeReq subscribeReq) {
        if (Validator.isNull(subscribeReq.getSubscriberId())) {
            throw new BadRequestException(ErrorCode.MSG1027);
        }

        final String clientId = SecurityUtils.getCurrentUserLogin().orElse(null);

        if (Validator.isNull(clientId)) {
            throw new BadRequestException(ErrorCode.MSG1028);
        }

        Customer customer = this.customerRepository.findByCustomerKey(getKey(clientId,  subscribeReq.getSubscriberId()));

        if (Validator.isNull(customer)) {
            throw new BadRequestException(ErrorCode.MSG1029);
        }

        List<CustomerData> data = this.customerDataRepository.findByCustomerIdAndStatus(customer.getId(), EntityStatus.ACTIVE.getStatus());

        return InfoRes.builder()
                .subscriberId(customer.getSubscriberId())
                .data(data.stream().map(r -> InfoRes.CustomerDataRes.builder()
                        .value(r.getValue())
                        .type(Objects.requireNonNull(CustomerDataType.valueOfStatus(r.getType())).name())
                        .verified(r.getVerify())
                        .build()).toList())
                .build();
    }

    @Override
    public TransactionResponse sendOtpToVerify(CustomerDataReq customerDataReq) {
        if (Validator.isNull(customerDataReq.getSubscriberId())) {
            throw new BadRequestException(ErrorCode.MSG1027);
        }

        final String clientId = SecurityUtils.getCurrentUserLogin().orElse(null);

        if (Validator.isNull(clientId)) {
            throw new BadRequestException(ErrorCode.MSG1028);
        }

        Customer customer = this.customerRepository.findByCustomerKey(getKey(clientId,  customerDataReq.getSubscriberId()));

        if (Validator.isNull(customer)) {
            throw new BadRequestException(ErrorCode.MSG1029);
        }

        CustomerData data = this.customerDataRepository.findByCustomerIdAndValueAndStatus(customer.getId(),
                customerDataReq.getValue(), EntityStatus.ACTIVE.getStatus());

        if (Validator.isNull(data)) {
            throw new BadRequestException(ErrorCode.MSG1033);
        }

        if (Validator.isNotNull(data.getVerify()) && Validator.equals(data.getVerify(), 1)) {
            throw new BadRequestException(ErrorCode.MSG1034);
        }

        String transactionId = "";

        if (Validator.equals(data.getType(), CustomerDataType.PHONE.getValue())) {
            transactionId = this.otpService.sendOtpViaSms(data.getValue(), OtpType.CUSTOMER_VERIFY, false);
        }

        if (Validator.equals(data.getType(), CustomerDataType.EMAIL.getValue())) {
//            transactionId = this.otpService.sendOtpViaSms(data.getValue(), OtpType.CUSTOMER_VERIFY, false);
        }

        return TransactionResponse.builder().transactionId(transactionId).build();
    }

    @Override
    public void verify(CustomerDataReq customerDataReq) {
        if (Validator.isNull(customerDataReq.getSubscriberId())) {
            throw new BadRequestException(ErrorCode.MSG1027);
        }

        final String clientId = SecurityUtils.getCurrentUserLogin().orElse(null);

        if (Validator.isNull(clientId)) {
            throw new BadRequestException(ErrorCode.MSG1028);
        }

        Customer customer = this.customerRepository.findByCustomerKey(getKey(clientId,  customerDataReq.getSubscriberId()));

        if (Validator.isNull(customer)) {
            throw new BadRequestException(ErrorCode.MSG1029);
        }

        CustomerData data = this.customerDataRepository.findByCustomerIdAndValueAndStatus(customer.getId(),
                customerDataReq.getValue(), EntityStatus.ACTIVE.getStatus());

        if (Validator.isNull(data)) {
            throw new BadRequestException(ErrorCode.MSG1033);
        }

        if (Validator.isNotNull(data.getVerify()) && Validator.equals(data.getVerify(), 1)) {
            throw new BadRequestException(ErrorCode.MSG1034);
        }

        if (Validator.equals(data.getType(), CustomerDataType.PHONE.getValue())) {
            this.otpService.validateOtp(data.getValue(), customerDataReq.getTransactionId(),
                    OtpType.CUSTOMER_VERIFY, customerDataReq.getOtp());
        }

        if (Validator.equals(data.getType(), CustomerDataType.EMAIL.getValue())) {
        }

        data.setVerify(1);

        this.customerDataRepository.save(data);
    }

    private String getKey(String clientId, String key) {
        return clientId + "_" + key;
    }
}
