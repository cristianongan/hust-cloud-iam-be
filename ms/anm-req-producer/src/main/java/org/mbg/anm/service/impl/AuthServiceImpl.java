package org.mbg.anm.service.impl;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mbg.anm.feign.AuthClient;
import org.mbg.anm.model.dto.request.SubscribeReq;
import org.mbg.anm.service.AuthService;
import org.mbg.common.api.exception.BadRequestException;
import org.mbg.common.base.enums.EntityStatus;
import org.mbg.common.base.enums.ErrorCode;
import org.mbg.common.base.model.Customer;
import org.mbg.common.base.model.JwtAccessToken;
import org.mbg.common.base.repository.CustomerRepository;
import org.mbg.common.util.Validator;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthClient authClient;

    private final CustomerRepository customerRepository;

    @Override
    public JwtAccessToken verifyCustomer(SubscribeReq req, String org) {
        if (Validator.isNull(org) || Validator.isNull(req.getSubscriberId())) {
            throw new BadRequestException(ErrorCode.MSG1029);
        }

        Customer cus = this.customerRepository
                .findByCustomerKeyAndStatusNot(String.format("%s_%s", org.toUpperCase(), req.getSubscriberId()), EntityStatus.DELETED.getStatus());

        if (Validator.isNull(cus)) {
            throw new BadRequestException(ErrorCode.MSG1029);
        }

        if (!Validator.equals(cus.getStatus(), EntityStatus.ACTIVE.getStatus())) {
            throw new BadRequestException(ErrorCode.MSG1029);
        }

        LocalDateTime now = LocalDateTime.now();

        if (Validator.isNotNull(cus.getStartTime()) && Validator.isNotNull(cus.getEndTime()) && (
                cus.getStartTime().isBefore(now) || cus.getEndTime().isAfter(now)
                )) {
            return authClient.generateToken(cus.getUserId());
        }

        throw new BadRequestException(ErrorCode.MSG1029);
    }
}
