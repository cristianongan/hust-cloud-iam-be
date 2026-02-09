package com.hust.iam.service;

import com.hust.iam.model.dto.request.CommonDataReq;
import com.hust.common.base.model.dto.CommonDataDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CommonDataService {
    List<CommonDataDTO> getAll(CommonDataReq req);

    Page<CommonDataDTO> get(CommonDataReq req);
}
