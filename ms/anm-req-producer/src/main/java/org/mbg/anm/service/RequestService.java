package org.mbg.anm.service;

import org.mbg.anm.model.dto.RequestDTO;

public interface RequestService {
    RequestDTO createRequest(RequestDTO requestDTO);

    RequestDTO updateRequest(RequestDTO requestDTO);

}
