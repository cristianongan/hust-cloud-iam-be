package com.hust.iam.service;

import com.hust.iam.model.dto.RecordDTO;
import com.hust.iam.model.dto.response.RecordTypeStatisticRes;
import com.hust.common.base.model.dto.request.RecordReq;
import org.springframework.data.domain.Page;

public interface RecordService {
    Page<RecordDTO> search(RecordReq req);

    RecordTypeStatisticRes recordTypeStatistic();
}
