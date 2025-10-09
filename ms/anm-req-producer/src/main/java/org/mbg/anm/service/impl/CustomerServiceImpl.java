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
import org.mbg.common.base.model.dto.response.TransactionResponse;
import org.mbg.common.base.repository.CustomerDataRepository;
import org.mbg.common.base.repository.CustomerRepository;
import org.mbg.anm.service.CustomerService;
import org.mbg.anm.service.mapper.CustomerMapper;
import org.mbg.common.base.enums.CustomerDataType;
import org.mbg.common.base.enums.EntityStatus;
import org.mbg.common.base.model.Customer;
import org.mbg.common.base.model.CustomerData;
import org.mbg.common.base.repository.RecordRepository;
import org.mbg.common.security.util.SecurityUtils;
import org.mbg.common.util.Validator;
import org.mbg.enums.OtpType;
import org.mbg.service.OtpService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

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

    private static final int EXTEND_LIMIT = 3;

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

        if (Validator.isNotNull(customer) && Validator.equals(customer.getStatus(), EntityStatus.ACTIVE.getStatus())) {
            throw new BadRequestException(ErrorCode.MSG1038);
        }

        if (Validator.isNotNull(customer) && Validator.equals(customer.getStatus(), EntityStatus.LOCK.getStatus())) {
            throw new BadRequestException(ErrorCode.MSG1005);
        }

        if (Validator.isNotNull(customer) && Validator.equals(customer.getStatus(), EntityStatus.INACTIVE.getStatus())) {
            customer.setStatus(EntityStatus.EXTEND.getStatus());
        }

        if (Validator.isNull(customer)) {
            customer = new Customer();
            customer.setCustomerKey(key);
            customer.setSubscriberId(subscribeReq.getSubscriberId());
            customer.setClientId(clientId);
            customer.setStatus(EntityStatus.ACTIVE.getStatus());
        }

        customer.setReference(subscribeReq.getReference());

        customer = this.customerRepository.save_(customer);

        if (Validator.equals(customer.getStatus(), EntityStatus.ACTIVE.getStatus())) {
            Set<String> keys = new HashSet<>();

            List<CustomerData> datas = new ArrayList<>();
            if (Validator.isNotNull(subscribeReq.getDataReqs())) {
                for (CustomerDataReq item : subscribeReq.getDataReqs()) {

                    if (Validator.isNull(item)) {
                        throw new BadRequestException(ErrorCode.MSG1030);
                    }

                    if (Validator.isNull(item.getStatus())) {
                        item.setStatus(EntityStatus.ACTIVE.getStatus());
                    } else if (!Validator.equals(item.getStatus(), EntityStatus.DELETED.getStatus())
                            && !Validator.equals(item.getStatus(), EntityStatus.ACTIVE.getStatus())) {
                        throw new BadRequestException(ErrorCode.MSG1031);
                    }

                    CustomerDataType type = CustomerDataType.resolveByName(item.getType());

                    if (Validator.isNull(type) || Validator.isNull(type.getValue())) {
                        throw new BadRequestException(ErrorCode.MSG1030);
                    }

                    if (keys.contains(item.getType())) {
                        throw new BadRequestException(ErrorCode.MSG1037);
                    }

                    keys.add(item.getType());

                    CustomerData customerData = new CustomerData();
                    customerData.setCustomerId(customer.getId());
                    customerData.setType(type.getValue());
                    customerData.setValue(item.getValue());
                    customerData.setStatus(EntityStatus.ACTIVE.getStatus());
                    customerData.setIsPrimary(1);
                    customerData.setSyncStatus(CustomerSyncStatus.NEW.getStatus());
                    customerData.setLastScan(LocalDateTime.MIN);

                    datas.add(customerData);
                }
            } else {
                throw new BadRequestException(ErrorCode.MSG1039);
            }

            this.customerDataRepository.saveAll(datas);
        }

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
        this.customerRepository.save_(customer);

        return SubscribeRes.builder().subscriberId(customer.getSubscriberId()).build();
    }

    @Override
    public LookupResponse lookup(LookupReq lookupReq) {
        if (Validator.isNull(lookupReq.getSubscriberId())) {
            throw new BadRequestException(ErrorCode.MSG1027);
        }

        Customer customer = this.getCus(lookupReq.getSubscriberId());

        lookupReq.setCustomerId(customer.getId());

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
                        .isPrimary(r.getIsPrimary())
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
            transactionId = this.otpService.sendOtpViaEmail(data.getValue(), OtpType.CUSTOMER_VERIFY, false);
        }

        return TransactionResponse.builder().transactionId(transactionId).build();
    }

    @Override
    public void verify(CustomerDataReq customerDataReq) {
        Customer customer = this.getCus(customerDataReq.getSubscriberId());

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

    @Override
    @Transactional
    public void addDataLookup(SubscribeReq subscribeReq) {
        Customer customer = this.getCus(subscribeReq.getSubscriberId());

        if (Validator.isNotNull(subscribeReq.getDataReqs())) {
            List<String> values =
                    subscribeReq.getDataReqs().stream().map(CustomerDataReq::getValue).filter(Validator::isNotNull).toList();

            if (Validator.isNull(values)) {
                throw new BadRequestException(ErrorCode.MSG1030);
            }

            List<CustomerData> old = this.customerDataRepository
                    .findByCustomerIdAndStatus(customer.getId(), EntityStatus.ACTIVE.getStatus());

            Set<String> oldSet = new HashSet<>();
            AtomicInteger dataCount = new AtomicInteger();

            if (Validator.isNotNull(old)) {
                old.forEach(oldData -> {
                    oldSet.add(oldData.getValue());

                    if (!Validator.equals(oldData.getIsPrimary(), 1)) {
                        dataCount.getAndIncrement();
                    }
                });

                if (dataCount.get() >= EXTEND_LIMIT) {
                    throw new BadRequestException(ErrorCode.MSG1040);
                }
            }

            List<CustomerData> data = new ArrayList<>();

            for (CustomerDataReq item: subscribeReq.getDataReqs()) {
                if (Validator.isNull(item)) {
                    throw new BadRequestException(ErrorCode.MSG1030);
                }

                if (Validator.isNull(item.getStatus())) {
                    item.setStatus(EntityStatus.ACTIVE.getStatus());
                } else if (!Validator.equals(item.getStatus(), EntityStatus.DELETED.getStatus())
                        && !Validator.equals(item.getStatus(), EntityStatus.ACTIVE.getStatus())) {
                    throw new BadRequestException(ErrorCode.MSG1031);
                }

                CustomerDataType type = CustomerDataType.resolveByName(item.getType());

                if (Validator.isNull(type) || Validator.isNull(type.getValue())) {
                    throw new BadRequestException(ErrorCode.MSG1030);
                }

                if (oldSet.contains(item.getValue())) {
                    throw new BadRequestException(ErrorCode.MSG1041);
                }

                if (dataCount.get() >= EXTEND_LIMIT) {
                    throw new BadRequestException(ErrorCode.MSG1040);
                }

                oldSet.add(item.getType());
                CustomerData customerData = new CustomerData();
                customerData.setCustomerId(customer.getId());
                customerData.setType(type.getValue());
                customerData.setValue(item.getValue());
                customerData.setStatus(EntityStatus.ACTIVE.getStatus());
                customerData.setIsPrimary(0);
                customerData.setSyncStatus(CustomerSyncStatus.NEW.getStatus());
                customerData.setLastScan(LocalDateTime.MIN);

                data.add(customerData);
                dataCount.getAndIncrement();
            }

            this.customerDataRepository.saveAll(data);
        }
    }

    @Override
    @Transactional
    public void removeDataLookup(SubscribeReq subscribeReq) {
        Customer customer = this.getCus(subscribeReq.getSubscriberId());

        if (Validator.isNotNull(subscribeReq.getDataReqs())) {
            List<String> values =
                    subscribeReq.getDataReqs().stream().map(CustomerDataReq::getValue).filter(Validator::isNotNull).toList();

            if (Validator.isNull(values)) {
                throw new BadRequestException(ErrorCode.MSG1030);
            }

            List<CustomerData> data = this.customerDataRepository.findByCustomerIdAndStatusAndValueIn(customer.getId(),
                    EntityStatus.ACTIVE.getStatus(), values);

            if (Validator.isNull(data)) {
                throw new BadRequestException(ErrorCode.MSG1042);
            }

            for (CustomerData item: data) {
                if (Validator.equals(item.getIsPrimary(), 1)) {
                    throw new BadRequestException(ErrorCode.MSG1043);
                }

                item.setStatus(EntityStatus.DELETED.getStatus());
            }

            this.customerDataRepository.saveAll(data);
        }
    }

    private Customer getCus(String subscriberId) {
        if (Validator.isNull(subscriberId)) {
            throw new BadRequestException(ErrorCode.MSG1027);
        }

        final String clientId = SecurityUtils.getCurrentUserLogin().orElse(null);

        if (Validator.isNull(clientId)) {
            throw new BadRequestException(ErrorCode.MSG1028);
        }

        Customer customer = this.customerRepository.findByCustomerKey(getKey(clientId, subscriberId));

        if (Validator.isNull(customer)) {
            throw new BadRequestException(ErrorCode.MSG1029);
        }

        return customer;
    }

    private String getKey(String clientId, String key) {
        return clientId + "_" + key;
    }
}
