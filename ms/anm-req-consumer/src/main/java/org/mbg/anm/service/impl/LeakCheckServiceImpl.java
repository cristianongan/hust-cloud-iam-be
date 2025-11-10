package org.mbg.anm.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mbg.anm.consumer.GroupIbApiSender;
import org.mbg.anm.service.CustomerDataService;
import org.mbg.common.base.repository.CustomerDataRepository;
import org.mbg.common.base.repository.RecordRepository;
import org.springframework.stereotype.Service;

@Slf4j
@Service("leakCheckService")
@RequiredArgsConstructor
public class LeakCheckServiceImpl implements CustomerDataService {
    private final CustomerDataRepository customerDataRepository;

    private final RecordRepository recordRepository;

    @Override
    public void craw(Long id, String api, String token, String dataSource) {

    }

    @Override
    public void onFail(Long id) {

    }
}
