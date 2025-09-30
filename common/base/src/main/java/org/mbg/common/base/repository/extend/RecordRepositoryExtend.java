package org.mbg.common.base.repository.extend;

import org.mbg.common.base.model.Customer;
import org.mbg.common.base.model.Record;
import org.mbg.common.base.model.dto.request.LookupReq;
import org.mbg.common.base.model.dto.request.RecordReq;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RecordRepositoryExtend {
    List<Record> search(LookupReq lookupReq);

    Long count(LookupReq lookupReq);

    List<Record> searchCms(RecordReq recordReq, Pageable pageable);

    Long countCms(RecordReq recordReq);
}
