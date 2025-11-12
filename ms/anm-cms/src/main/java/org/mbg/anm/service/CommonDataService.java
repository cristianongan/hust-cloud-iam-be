package org.mbg.anm.service;

import org.mbg.anm.model.dto.request.CommonDataReq;
import org.mbg.common.base.model.dto.CommonDataDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CommonDataService {
    List<CommonDataDTO> getAll(CommonDataReq req);

    Page<CommonDataDTO> get(CommonDataReq req);
}
