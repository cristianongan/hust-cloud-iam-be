package org.mbg.common.base.repository.extend;

import org.mbg.common.base.enums.CustomerSyncStatus;
import org.mbg.common.base.enums.EntityStatus;
import org.mbg.common.base.model.CustomerData;

import java.util.List;

public interface CustomerDataRepositoryExtend {
    List<CustomerData> getLookupData(CustomerSyncStatus customerSyncStatus, EntityStatus status, Integer limit);
}
