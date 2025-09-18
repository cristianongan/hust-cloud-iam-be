package org.mbg.anm.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mbg.anm.repository.ProducerRequestRepository;
import org.mbg.anm.service.RequestService;
import org.mbg.anm.service.mapper.ProducerRequestMapper;
import org.mbg.common.api.exception.BadRequestException;
import org.mbg.common.base.enums.RequestStatus;
import org.mbg.common.base.model.ProducerRequest;
import org.mbg.common.base.model.dto.ProducerRequestDTO;
import org.mbg.common.label.LabelKey;
import org.mbg.common.label.Labels;
import org.mbg.common.security.util.SecurityConstants;
import org.mbg.common.security.util.SecurityUtils;
import org.mbg.common.util.Validator;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class RequestServiceImpl implements RequestService {
    private final ProducerRequestMapper producerRequestMapper;

    private final ProducerRequestRepository producerRequestRepository;

    @Override
    public ProducerRequestDTO createRequest(ProducerRequestDTO requestDTO) {
        if (Validator.isNull(requestDTO.getRequestId())) {
            requestDTO.setRequestId(MDC.get(SecurityConstants.Header.REQUEST_ID_MDC));
        }

        if (Validator.isNull(requestDTO.getPhone()) && Validator.isNull(requestDTO.getEmail())
            && Validator.isNull(requestDTO.getPhone())) {
            throw new BadRequestException(Labels.getLabels(LabelKey.ERROR_INPUT_CANNOT_BE_EMPTY, new String[]{Labels.getLabels(LabelKey.LABEL_FILTER)}),
                    ProducerRequestDTO.class.getName(), LabelKey.ERROR_INPUT_CANNOT_BE_EMPTY);
        }

        ProducerRequest req = this.producerRequestMapper.toEntity(requestDTO);
        req.setClientId(SecurityUtils.getCurrentUserLogin().orElse(null));
        req.setStatus(RequestStatus.NEW.getStatus());

        return this.producerRequestMapper.toDto(this.producerRequestRepository.save(req));
    }

}
