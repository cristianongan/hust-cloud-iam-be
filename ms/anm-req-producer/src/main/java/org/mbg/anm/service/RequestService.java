package org.mbg.anm.service;

import org.mbg.common.base.model.dto.ProducerRequestDTO;

public interface RequestService {
    ProducerRequestDTO createRequest(ProducerRequestDTO requestDTO);

    ProducerRequestDTO updateRequest(ProducerRequestDTO requestDTO);

}
