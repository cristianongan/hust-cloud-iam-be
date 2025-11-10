package org.mbg.anm.service.impl;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mbg.anm.consumer.GroupIbApiSender;
import org.mbg.anm.consumer.request.GroupIbReq;
import org.mbg.anm.consumer.response.GroupIbCompromisedRes;
import org.mbg.anm.service.CustomerDataService;
import org.mbg.common.base.enums.CustomerDataType;
import org.mbg.common.base.enums.CustomerSyncStatus;
import org.mbg.common.base.enums.EntityStatus;
import org.mbg.common.base.enums.LeakSeverity;
import org.mbg.common.base.model.CustomerData;
import org.mbg.common.base.model.Record;
import org.mbg.common.base.repository.CustomerDataRepository;
import org.mbg.common.base.repository.CustomerRepository;
import org.mbg.common.base.repository.RecordRepository;
import org.mbg.common.util.DateUtil;
import org.mbg.common.util.Validator;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
@Primary
public class GroupIbServiceImpl implements CustomerDataService {
    private final CustomerDataRepository customerDataRepository;

    private final GroupIbApiSender groupIbApiSender;

    private final RecordRepository recordRepository;

    private final CustomerRepository customerRepository;

    private final Gson gson;


    @Override
    public void craw(Long id, String api, String token, String dataSource) {
        if (Validator.isNull(id)) {
            return;
        }

        CustomerData dataItem = this.customerDataRepository.findByIdAndStatus(id, EntityStatus.ACTIVE.getStatus());

        if (Validator.isNull(dataItem)) {
            return;
        }

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        StringBuilder filter = new StringBuilder();
        if (Validator.equals(dataItem.getType(), CustomerDataType.EMAIL.getValue())) {
            filter.append("email:");
        }
        if (Validator.equals(dataItem.getType(), CustomerDataType.PHONE.getValue())) {
            filter.append("phone:");
        }
        filter.append(dataItem.getValue());

        GroupIbReq groupIbReq = new GroupIbReq();
        groupIbReq.setMethod(HttpMethod.GET);
        groupIbReq.setBaseUrl(api);

        params.add("q", filter.toString());
//        params.add("offset", "0");
//        params.add("limit", "100");

        GroupIbCompromisedRes response =  groupIbApiSender.sendToGroupIb(groupIbReq,
                GroupIbCompromisedRes.class, params, token);

        if (Validator.isNull(response)) {
            return;
        }

        if (Validator.isNotNull(response.getItems())) {
            List<org.mbg.common.base.model.Record> records = new ArrayList<>();
            response.getItems().forEach(item -> {
                String leakId = Validator.isNotNull(item.getId()) ? item.getId().getFirst() : null;
                if (!recordRepository.existsByCustomerKeyAndLeakId(dataItem.getCustomerKey(), leakId)) {
                    org.mbg.common.base.model.Record record = new Record(dataItem.getCustomerKey() , response.getResultId(),leakId, dataSource,
                            item.getLeakName(), DateUtil.utcToTimeStampSecond(item.getLeakPublished()),
                            DateUtil.utcToTimeStampSecond(item.getUploadTime()),
                            Validator.isNotNull(item.getEvaluation()) ? resolveSeverityGroupIb(item.getEvaluation().getSeverity()) : null ,
                            this.gson.fromJson(item.getAddInfo(), new TypeToken<Map<String, Object>>(){}.getType()),
                            item.getDescription()
                    );

                    record.setDataLookup(dataItem.getValue());

                    records.add(record);
                }
            });

            this.recordRepository.saveAll(records);
        }

        dataItem.setSyncStatus(CustomerSyncStatus.UPDATED.getStatus());
        dataItem.setLastScan(LocalDateTime.now());

        this.customerDataRepository.save(dataItem);
    }

    @Override
    public void onFail(Long id) {
        if (Validator.isNull(id)) {
            return;
        }

        CustomerData dataItem = this.customerDataRepository.findByIdAndStatus(id, EntityStatus.ACTIVE.getStatus());

        if (Validator.isNull(dataItem)) {
            return;
        }

        dataItem.setSyncStatus(CustomerSyncStatus.UPDATED.getStatus());
        dataItem.setLastScan(LocalDateTime.now());

        this.customerDataRepository.save(dataItem);
    }

    private Integer resolveSeverityGroupIb(String input) {
        return switch (input) {
            case "green" -> LeakSeverity.LOW.getValue();
            case "yellow" -> LeakSeverity.MEDIUM.getValue();
            case "orange" -> LeakSeverity.HIGH.getValue();
            case "red" -> LeakSeverity.CRITICAL.getValue();
            default -> null;
        };

    }

}
