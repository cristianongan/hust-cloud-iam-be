package com.hust.iam.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.hust.iam.model.dto.RecordDTO;
import com.hust.iam.model.dto.response.RecordTypeStatisticRes;
import com.hust.common.api.exception.BadRequestException;
import com.hust.common.base.model.dto.request.RecordReq;
import com.hust.iam.service.RecordService;
import com.hust.iam.service.mapper.RecordMapper;
import com.hust.common.base.repository.RecordRepository;
import com.hust.common.label.LabelKey;
import com.hust.common.label.Labels;
import com.hust.common.util.Validator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecordServiceImpl implements RecordService {
    private final RecordRepository recordRepository;

    private final RecordMapper recordMapper;

    @Override
    public Page<RecordDTO> search(RecordReq req) {
        if (Validator.isNull(req.getDetectTimeEnd())) {
            throw new BadRequestException(Labels.getLabels(LabelKey.ERROR_INPUT_CANNOT_BE_EMPTY,
                    new String[]{Labels.getLabels(LabelKey.LABEL_END_TIME)}),
                    Record.class.getName(), LabelKey.ERROR_INPUT_CANNOT_BE_EMPTY);
        }

        if (Validator.isNull(req.getDetectTimeStart())) {
            throw new BadRequestException(Labels.getLabels(LabelKey.ERROR_INPUT_CANNOT_BE_EMPTY,
                    new String[]{Labels.getLabels(LabelKey.LABEL_START_TIME)}),
                    Record.class.getName(), LabelKey.ERROR_INPUT_CANNOT_BE_EMPTY);
        }

        Pageable pageable = PageRequest.of(req.getPage(), req.getPageSize());

        List<com.hust.common.base.model.Record> records = this.recordRepository.searchCms(req, pageable);

        List<RecordDTO> content = this.recordMapper.toDto(records);

        Long count  = this.recordRepository.countCms(req);

        return new PageImpl<>(content, pageable, count);
    }

    @Override
    public RecordTypeStatisticRes recordTypeStatistic() {


        return null;
    }
}
