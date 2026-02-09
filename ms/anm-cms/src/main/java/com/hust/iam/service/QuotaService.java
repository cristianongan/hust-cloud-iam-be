package com.hust.iam.service;

import com.hust.iam.model.dto.request.ChangeTotalQuotaReq;
import com.hust.common.base.model.dto.QuotaDTO;
import com.hust.common.base.model.dto.request.QuotaBatchReq;
import com.hust.common.base.model.dto.response.IncreaseInUseRes;

import java.util.List;

public interface QuotaService {
    void changeTotalQuota(ChangeTotalQuotaReq req);

    IncreaseInUseRes increase(Long userId);

    List<QuotaDTO> getBatch(QuotaBatchReq req);
}
