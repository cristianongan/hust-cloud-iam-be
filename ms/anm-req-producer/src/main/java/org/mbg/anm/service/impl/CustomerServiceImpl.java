package org.mbg.anm.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mbg.anm.configuration.ProducerProperties;
import org.mbg.anm.feign.AuthClient;
import org.mbg.anm.feign.CmsClient;
import org.mbg.anm.model.dto.request.CustomerDataReq;
import org.mbg.anm.model.dto.request.SubscribeBatchReq;
import org.mbg.anm.model.dto.response.*;
import org.mbg.anm.service.mapper.RecordMapper;
import org.mbg.common.api.exception.BadRequestException;
import org.mbg.common.base.configuration.ValidationProperties;
import org.mbg.common.base.enums.*;
import org.mbg.common.base.model.CustomerLog;
import org.mbg.common.base.model.Record;
import org.mbg.common.base.model.dto.UserDTO;
import org.mbg.common.base.model.dto.request.LookupReq;
import org.mbg.anm.model.dto.request.SubscribeReq;
import org.mbg.common.base.model.dto.request.UserBatchReq;
import org.mbg.common.base.model.dto.request.UserReq;
import org.mbg.common.base.model.dto.response.CustomerUserBatchRes;
import org.mbg.common.base.model.dto.response.CustomerUserRes;
import org.mbg.common.base.model.dto.response.TransactionResponse;
import org.mbg.common.base.repository.CustomerDataRepository;
import org.mbg.common.base.repository.CustomerLogRepository;
import org.mbg.common.base.repository.CustomerRepository;
import org.mbg.anm.service.CustomerService;
import org.mbg.anm.service.mapper.CustomerMapper;
import org.mbg.common.base.model.Customer;
import org.mbg.common.base.model.CustomerData;
import org.mbg.common.base.repository.RecordRepository;
import org.mbg.common.base.service.mapper.CustomerLogMapper;
import org.mbg.common.label.LabelKey;
import org.mbg.common.label.Labels;
import org.mbg.common.security.RsaProvider;
import org.mbg.common.security.configuration.AuthenticationProperties;
import org.mbg.common.security.exception.UnauthorizedException;
import org.mbg.common.security.util.SecurityUtils;
import org.mbg.common.util.Validator;
import org.mbg.enums.OtpType;
import org.mbg.service.EmailService;
import org.mbg.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    private final CustomerDataRepository customerDataRepository;

    private final RecordRepository recordRepository;

    private final RecordMapper recordMapper;

    private final OtpService otpService;

    private final CmsClient cmsClient;

    private final AuthClient authClient;

    private final ProducerProperties properties;

    private final CustomerLogMapper customerLogMapper;

    private final CustomerLogRepository customerLogRepository;

    @Autowired
    @Qualifier("clientRsaProvider")
    private RsaProvider rsaProvider;

    private final ValidationProperties validationProperties;

    private final EmailService emailService;

    private static final int EXTEND_LIMIT = 3;

    @Override
    @Transactional
    public SubscribeBatchRes subscribeBatch(SubscribeBatchReq req, String org) {
        final String clientId = org;

        if (Validator.isNull(clientId)) {
            throw new BadRequestException(ErrorCode.MSG1028);
        }

        if (Validator.isNull(req) || Validator.isNull(req.getDataReqs())) {
            throw new BadRequestException(ErrorCode.MSG1016);
        }

        if (req.getDataReqs().size() > this.properties.getSubscribeBatchLimit()) {
            throw new BadRequestException(ErrorCode.MSG1046);
        }

        List<String> cusKeys = req.getDataReqs().parallelStream().map(r ->
                    getKey(clientId, r.getSubscriberId())
                ).toList();

        List<Customer> customers =
                this.customerRepository.findByCustomerKeyInAndStatusNot(cusKeys, EntityStatus.DELETED.getStatus());

        Map<String, Customer> customerMap = new HashMap<>();

        List<Customer> entities = new ArrayList<>();
        List<CustomerData> data = new ArrayList<>();

        if (!customers.isEmpty()) {
            for (Customer customer : customers) {
                customerMap.put(customer.getCustomerKey(), customer);
            }
        }


        List<UserReq> userReqs = new ArrayList<>();

        LocalDateTime now = LocalDateTime.now();
        ZoneId zone = ZoneId.of("Asia/Bangkok");

        for (SubscribeBatchReq.DataReq item : req.getDataReqs()) {
            if (Validator.isNotNull(item)) {
                if (Validator.isNull(item.getSubscriberId())) {
                    throw new BadRequestException(ErrorCode.MSG1027);
                }

                if (Validator.isNull(item.getStartTime()) || Validator.isNull(item.getEndTime())) {
                    throw new BadRequestException(ErrorCode.MSG1048);
                }

                if (item.getStartTime() >= item.getEndTime()) {
                    throw new BadRequestException(ErrorCode.MSG1049);
                }

                final String key = this.getKey(clientId, item.getSubscriberId());

                Customer customer = customerMap.get(key);
                String phone = "";
                String email = "";
                LocalDateTime start = LocalDateTime.ofInstant(Instant.ofEpochMilli(item.getStartTime()), zone);
                LocalDateTime end = LocalDateTime.ofInstant(Instant.ofEpochMilli(item.getEndTime()), zone);

                if (start.isBefore(end)) {
                    throw new BadRequestException(ErrorCode.MSG1051);
                }

                if (Validator.isNotNull(customer) && Validator.equals(customer.getStatus(), EntityStatus.ACTIVE.getStatus())
                    && customer.getEndTime().isAfter(now)) {
                    throw new BadRequestException(ErrorCode.MSG1038);
                }

                if (Validator.isNotNull(customer) && Validator.equals(customer.getStatus(), EntityStatus.LOCK.getStatus())) {
                    throw new BadRequestException(ErrorCode.MSG1005);
                }

                if (Validator.isNotNull(customer) && Validator.equals(customer.getStatus(), EntityStatus.INACTIVE.getStatus())) {
                    customer.setStatus(EntityStatus.ACTIVE.getStatus());
                }

                boolean isCreate = false;

                if (Validator.isNull(customer)) {
                    customer = new Customer();
                    customer.setCustomerKey(key);
                    customer.setSubscriberId(item.getSubscriberId());
                    customer.setStatus(EntityStatus.ACTIVE.getStatus());
                    isCreate = true;
                }

                customer.setReference(req.getReference());
                customer.setStartTime(start);
                customer.setEndTime(end);

                if (Validator.isNotNull(item.getType()) && isCreate) {
                    CustomerData customerData = null;
                    if (item.getType().equalsIgnoreCase(CustomerDataType.PHONE.name())) {
                        customerData = new CustomerData();
                        customerData.setType(CustomerDataType.PHONE.getValue());

                        phone = customer.getSubscriberId();
                    }

                    if (item.getType().equalsIgnoreCase(CustomerDataType.EMAIL.name())) {
                        customerData = new CustomerData();
                        customerData.setType(CustomerDataType.EMAIL.getValue());
                        email = customer.getSubscriberId();
                    }

                    if (customerData != null) {
                        customerData.setVerify(1);
                        customerData.setCustomerKey(key);
                        customerData.setValue(customer.getSubscriberId());
                        customerData.setIsPrimary(1);

                        data.add(customerData);
                    }
                }

                entities.add(customer);
                if (isCreate) {
                    String password = this.validationProperties.generateRandomPassword();
                    String passEncrypted = null;
                    try {
                        passEncrypted = this.rsaProvider.encrypt(password);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    userReqs.add(UserReq.builder()
                            .username(customer.getCustomerKey())
                            .password(passEncrypted)
                            .phone(phone)
                            .email(email)
                            .build());
                }
            }
        }

        CustomerUserBatchRes res = this.authClient.createCustomerUser(UserBatchReq.builder()
                .users(userReqs)
                .build()
        );

        if (Validator.isNull(res)) {
            throw new BadRequestException(ErrorCode.MSG1017);
        }

        List<CustomerUserRes> customerUserRes = res.getCustomerUserRes();
        Map<String, Long> customerResMap = customerUserRes.stream().collect(Collectors.toMap(
                CustomerUserRes::getUsername, CustomerUserRes::getUserId
        ));

        for (Customer cus : entities) {
            if (customerResMap.containsKey(cus.getCustomerKey())) {
                cus.setUserId(customerResMap.get(cus.getCustomerKey()));
            }
        }

        this.customerDataRepository.saveAll(data);
        entities = this.customerRepository.saveAll(entities);

        List<CustomerLog> logs = this.customerLogMapper.toDto(entities);
        this.customerLogRepository.saveAll(logs);

        return null;
    }

    @Override
    @Transactional
    public SubscribeRes subscribe(SubscribeReq subscribeReq) {
        final String clientId = SecurityUtils.getCurrentUserLogin().orElse(null);

        if (Validator.isNull(clientId)) {
            throw new BadRequestException(ErrorCode.MSG1028);
        }

        if (Validator.isNull(subscribeReq.getCustomerKey())) {
            throw new BadRequestException(ErrorCode.MSG1027);
        }

        final String key = subscribeReq.getCustomerKey();

        Customer customer = this.customerRepository.findByCustomerKeyAndStatusNot(key, EntityStatus.DELETED.getStatus());

        if (Validator.isNotNull(customer) && Validator.equals(customer.getStatus(), EntityStatus.ACTIVE.getStatus())) {
            throw new BadRequestException(ErrorCode.MSG1038);
        }

        if (Validator.isNotNull(customer) && Validator.equals(customer.getStatus(), EntityStatus.LOCK.getStatus())) {
            throw new BadRequestException(ErrorCode.MSG1005);
        }

        if (Validator.isNotNull(customer) && Validator.equals(customer.getStatus(), EntityStatus.INACTIVE.getStatus())) {
            customer.setStatus(EntityStatus.ACTIVE.getStatus());
        }

        boolean isCreateUser = false;

        if (Validator.isNull(customer)) {
            customer = new Customer();
            customer.setCustomerKey(key);
            customer.setSubscriberId(subscribeReq.getSubscriberId());
            customer.setStatus(EntityStatus.ACTIVE.getStatus());
            isCreateUser = true;
        }

        customer.setReference(subscribeReq.getReference());

        customer = this.customerRepository.save(customer);

        if (Validator.equals(customer.getStatus(), EntityStatus.ACTIVE.getStatus()) && isCreateUser) {
            Set<String> keys = new HashSet<>();

            String phone = "";
            String email = "";

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

                    if (Validator.equals(type.getValue(), CustomerDataType.PHONE.getValue())) {
                        phone = item.getValue();
                    }

                    if (Validator.equals(type.getValue(), CustomerDataType.EMAIL.getValue())) {
                        email = item.getValue();
                    }

                    if (keys.contains(item.getType())) {
                        throw new BadRequestException(ErrorCode.MSG1037);
                    }

                    keys.add(item.getType());

                    CustomerData customerData = new CustomerData();
                    customerData.setCustomerKey(customer.getCustomerKey());
                    customerData.setType(type.getValue());
                    customerData.setValue(item.getValue());
                    customerData.setStatus(EntityStatus.ACTIVE.getStatus());
                    customerData.setIsPrimary(1);
                    customerData.setSyncStatus(CustomerSyncStatus.NEW.getStatus());

                    datas.add(customerData);
                }
            } else {
                throw new BadRequestException(ErrorCode.MSG1039);
            }

            this.customerDataRepository.saveAll(datas);

//            String username;
//            String password;
//            String passEncrypted;

//            try {
//                username = customer.getSubscriberId();
//                password = this.validationProperties.generateRandomPassword();
//                passEncrypted = this.rsaProvider.encrypt(password);
//
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }

//            this.authClient.createCustomerUser(UserReq.builder()
//                    .username(username)
//                    .password(passEncrypted)
//                    .phone(phone)
//                    .email(email)
//                    .build());

//            if (Validator.isNotNull(email)) {
//                Map<String, String> valuesMap = new HashMap<>();
//
//                valuesMap.put(TemplateField._USERNAME_.name(), username);
//                valuesMap.put(TemplateField.PASSWORD.name(), password);
//
//                this.emailService.send(email, TemplateCode.EMAIL_NEW_CUSTOMER.name(), valuesMap);
//            }
        }

        return SubscribeRes.builder().subscriberId(customer.getSubscriberId()).build();
    }

    @Override
    public SubscribeRes unSubscribe(SubscribeReq subscribeReq) {
        final String clientId = SecurityUtils.getCurrentUserLogin().orElse(null);

        if (Validator.isNull(clientId)) {
            throw new BadRequestException(ErrorCode.MSG1028);
        }

        final String key = subscribeReq.getCustomerKey();

        Customer customer = this.customerRepository.findByCustomerKeyAndStatusNot(key, EntityStatus.DELETED.getStatus());

        if (Validator.isNull(customer)) {
            throw new BadRequestException(ErrorCode.MSG1027);
        }

        customer.setStatus(EntityStatus.INACTIVE.getStatus());
        this.customerRepository.save_(customer);

        return SubscribeRes.builder().subscriberId(customer.getSubscriberId()).build();
    }

    @Override
    public LookupResponse lookup(LookupReq lookupReq) {
        if (Validator.isNull(lookupReq.getCustomerKey())) {
            throw new BadRequestException(ErrorCode.MSG1027);
        }

        final String clientId = SecurityUtils.getCurrentUserLogin().orElse(null);

        Customer customer = this.getCus(lookupReq.getCustomerKey());

        lookupReq.setCustomerKey(customer.getCustomerKey());

        List<Record> records = this.recordRepository.search(lookupReq);

        List<RecordResponse> content = this.recordMapper.toDto(records);

        return LookupResponse.builder()
                .data(content)
                .customerKey(lookupReq.getCustomerKey())
                .build();
    }

    @Override
    public LookupResponse lookup(LookupReq lookupReq, String clientId) {
        if (Validator.isNull(lookupReq.getCustomerKey())) {
            throw new BadRequestException(ErrorCode.MSG1027);
        }

        Customer customer = this.getCus(lookupReq.getCustomerKey());

        lookupReq.setCustomerKey(customer.getCustomerKey());

        List<Record> records = this.recordRepository.search(lookupReq);

        List<RecordResponse> content = this.recordMapper.toDto(records);

        return LookupResponse.builder()
                .data(content)
                .customerKey(lookupReq.getCustomerKey())
                .build();
    }

    @Override
    public InfoRes info() {
        UserDTO dto = authClient.detail();

        if (Validator.isNull(dto)) {
            throw new BadRequestException(ErrorCode.MSG1027);
        }

        Customer customer = this.customerRepository.findByUserIdAndStatusNot(dto.getId(),
                EntityStatus.DELETED.getStatus());

        if (Validator.isNull(customer)) {
            throw new BadRequestException(ErrorCode.MSG1029);
        }

        List<CustomerData> data = this.customerDataRepository.findByCustomerKeyAndStatus(customer.getCustomerKey(), EntityStatus.ACTIVE.getStatus());

        return InfoRes.builder()
                .customerKey(customer.getCustomerKey())
                .data(data.stream().map(r -> InfoRes.CustomerDataRes.builder()
                        .value(r.getValue())
                        .type(Objects.requireNonNull(CustomerDataType.valueOf(r.getType())).name())
                        .isPrimary(r.getIsPrimary())
                        .verified(r.getVerify())
                        .build()).toList())
                .build();
    }

    @Override
    public TransactionResponse sendOtpToVerify(CustomerDataReq customerDataReq, String clientId) {
        if (Validator.isNull(customerDataReq.getCustomerKey())) {
            throw new BadRequestException(ErrorCode.MSG1027);
        }

        if (Validator.isNull(clientId)) {
            throw new BadRequestException(ErrorCode.MSG1028);
        }

        Customer customer = this.customerRepository.findByCustomerKeyAndStatusNot(customerDataReq.getCustomerKey(),
                EntityStatus.DELETED.getStatus());

        if (Validator.isNull(customer)) {
            throw new BadRequestException(ErrorCode.MSG1029);
        }

        CustomerData data = this.customerDataRepository.findByCustomerKeyAndValueAndStatus(customer.getCustomerKey(),
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
    public void verify(CustomerDataReq customerDataReq, String clientId) {
        Customer customer = this.getCus(customerDataReq.getCustomerKey());

        CustomerData data = this.customerDataRepository.findByCustomerKeyAndValueAndStatus(customer.getCustomerKey(),
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
    public void addDataLookup(SubscribeReq subscribeReq, String clientId) {
        Customer customer = this.getCus(subscribeReq.getCustomerKey());

        if (Validator.isNotNull(subscribeReq.getDataReqs())) {
            List<String> values =
                    subscribeReq.getDataReqs().stream().map(CustomerDataReq::getValue).filter(Validator::isNotNull).toList();

            if (Validator.isNull(values)) {
                throw new BadRequestException(ErrorCode.MSG1030);
            }

            List<CustomerData> old = this.customerDataRepository
                    .findByCustomerKeyAndStatus(customer.getCustomerKey(), EntityStatus.ACTIVE.getStatus());

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

                if (oldSet.contains(item.getValue())) {
                    throw new BadRequestException(ErrorCode.MSG1041);
                }

                if (dataCount.get() >= EXTEND_LIMIT) {
                    throw new BadRequestException(ErrorCode.MSG1040);
                }

                oldSet.add(item.getType());
                CustomerData customerData = new CustomerData();
                customerData.setCustomerKey(customer.getCustomerKey());
                customerData.setType(type.getValue());
                customerData.setValue(item.getValue());
                customerData.setStatus(EntityStatus.ACTIVE.getStatus());
                customerData.setIsPrimary(0);
                customerData.setSyncStatus(CustomerSyncStatus.NEW.getStatus());

                data.add(customerData);
                dataCount.getAndIncrement();
            }

            this.customerDataRepository.saveAll(data);
        }
    }

    @Override
    @Transactional
    public void removeDataLookup(SubscribeReq subscribeReq, String clientId) {
        Customer customer = this.getCus(subscribeReq.getCustomerKey());

        if (Validator.isNotNull(subscribeReq.getDataReqs())) {
            List<String> values =
                    subscribeReq.getDataReqs().stream().map(CustomerDataReq::getValue).filter(Validator::isNotNull).toList();

            if (Validator.isNull(values)) {
                throw new BadRequestException(ErrorCode.MSG1030);
            }

            List<CustomerData> data = this.customerDataRepository.findByCustomerKeyAndStatusAndValueIn(customer.getCustomerKey(),
                    EntityStatus.ACTIVE.getStatus(), values);

            if (Validator.isNull(data)) {
                throw new BadRequestException(ErrorCode.MSG1042);
            }

            for (CustomerData item : data) {
                if (Validator.equals(item.getIsPrimary(), 1)) {
                    throw new BadRequestException(ErrorCode.MSG1043);
                }

                item.setStatus(EntityStatus.DELETED.getStatus());
            }

            this.customerDataRepository.saveAll(data);
        }
    }

    private Customer getCus(String customerKey) {
        if (Validator.isNull(customerKey)) {
            throw new BadRequestException(ErrorCode.MSG1027);
        }


        Customer customer = this.customerRepository.findByCustomerKeyAndStatusNot(customerKey,
                EntityStatus.DELETED.getStatus());

        if (Validator.isNull(customer)) {
            throw new BadRequestException(ErrorCode.MSG1029);
        }

        return customer;
    }

    private String getKey(String clientId, String key) {
        return clientId + "_" + key;
    }
}
