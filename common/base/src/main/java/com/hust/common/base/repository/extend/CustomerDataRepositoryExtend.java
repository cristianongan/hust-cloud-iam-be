package com.hust.common.base.repository.extend;

import com.hust.common.base.enums.CustomerSyncStatus;
import com.hust.common.base.enums.EntityStatus;
import com.hust.common.base.model.CustomerData;

import java.util.List;

public interface CustomerDataRepositoryExtend {
    List<CustomerData> getLookupData(CustomerSyncStatus customerSyncStatus, EntityStatus status, Integer limit);
}
