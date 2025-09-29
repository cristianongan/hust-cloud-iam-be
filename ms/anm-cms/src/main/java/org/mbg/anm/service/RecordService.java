package org.mbg.anm.service;

import org.mbg.anm.model.dto.RecordDTO;
import org.mbg.common.base.model.dto.request.RecordReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RecordService {
    Page<RecordDTO> search(RecordReq req);
}
