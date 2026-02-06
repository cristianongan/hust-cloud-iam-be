package com.hust.iam.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.hust.iam.model.dto.request.CommonDataReq;
import com.hust.iam.service.CommonDataService;
import org.mbg.common.api.exception.BadRequestException;
import org.mbg.common.base.enums.EntityStatus;
import org.mbg.common.base.enums.ErrorCode;
import org.mbg.common.base.model.CommonData;
import org.mbg.common.base.model.dto.CommonDataDTO;
import org.mbg.common.base.repository.CommonDataRepository;
import org.mbg.common.base.service.mapper.CommonDataMapper;
import org.mbg.common.util.Validator;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class CommonDataServiceImpl implements CommonDataService {

    private final CommonDataRepository commonDataRepository;

    private final CommonDataMapper commonDataMapper;

    @Override
    public List<CommonDataDTO> getAll(CommonDataReq req) {
        if (Validator.isNull(req.getType())) {
            throw new BadRequestException(ErrorCode.MSG1016);
        }

        List<CommonData> content = this.commonDataRepository.findByTypeAndStatus(req.getType(), EntityStatus.ACTIVE.getStatus());
        return this.commonDataMapper.toDto(content);
    }

    @Override
    public Page<CommonDataDTO> get(CommonDataReq req) {
        return null;
    }
}
