package org.mbg.anm.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mbg.anm.repository.ProducerRequestRepository;
import org.mbg.anm.service.RequestService;
import org.mbg.anm.service.mapper.ProducerRequestMapper;
import org.mbg.common.base.model.dto.ProducerRequestDTO;
import org.mbg.common.util.Validator;
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

        }

        return null;
    }

}
