package org.mbg.anm.service.impl;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mbg.anm.consumer.GroupIbApiSender;
import org.mbg.anm.consumer.LeakCheckApiSender;
import org.mbg.anm.consumer.request.GroupIbReq;
import org.mbg.anm.consumer.request.LeakCheckReq;
import org.mbg.anm.consumer.response.GroupIbCompromisedRes;
import org.mbg.anm.consumer.response.LeakCheckResponse;
import org.mbg.anm.service.CustomerDataService;
import org.mbg.common.base.enums.*;
import org.mbg.common.base.model.ContentTemplate;
import org.mbg.common.base.model.CustomerData;
import org.mbg.common.base.model.Record;
import org.mbg.common.base.repository.ContentTemplateRepository;
import org.mbg.common.base.repository.CustomerDataRepository;
import org.mbg.common.base.repository.RecordRepository;
import org.mbg.common.base.util.PiiScanner;
import org.mbg.common.label.LabelKey;
import org.mbg.common.label.Labels;
import org.mbg.common.security.util.SecurityConstants;
import org.mbg.common.util.DateUtil;
import org.mbg.common.util.Md5Util;
import org.mbg.common.util.StringUtil;
import org.mbg.common.util.Validator;
import org.slf4j.MDC;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.*;

@Slf4j
@Service("leakCheckService")
@RequiredArgsConstructor
public class LeakCheckServiceImpl implements CustomerDataService {
    private final CustomerDataRepository customerDataRepository;

    private final RecordRepository recordRepository;

    private final LeakCheckApiSender leakCheckApiSender;

    private final ContentTemplateRepository contentTemplateRepository;

    private static final ZoneId zone = ZoneId.of("Asia/Ho_Chi_Minh");

    @Override
    public void craw(Long id, String api, String token, String dataSource) {
        if (Validator.isNull(id)) {
            return;
        }

        CustomerData dataItem = this.customerDataRepository.findByIdAndStatus(id, EntityStatus.ACTIVE.getStatus());

        ContentTemplate contentTemplate = this.contentTemplateRepository
                .findByTemplateCodeAndStatus(TemplateCode.DATA_DESCRIPTION.toString(), EntityStatus.ACTIVE.getStatus());

        if (Validator.isNull(dataItem)) {
            return;
        }

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        LeakCheckReq leakCheckReq = new LeakCheckReq();
        leakCheckReq.setMethod(HttpMethod.GET);
        leakCheckReq.setBaseUrl(api);

        params.add("check", dataItem.getValue());
        CustomerDataType customerDataType = CustomerDataType.valueOf(dataItem.getType());

        LeakCheckResponse response =  leakCheckApiSender.sendToLeakCheck(leakCheckReq,
                LeakCheckResponse.class, params, token);

        if (Validator.isNull(response)) {
            return;
        }

        if (Validator.isNotNull(response.getSources())) {
            List<String> types = resolveType(response.getFields());

            if (Validator.isNotNull(customerDataType)) {
                types.add(customerDataType.toString().toLowerCase());
            }

            List<String> unique = new ArrayList<>(new LinkedHashSet<>(types));


            String typeNom = StringUtil.join(unique.stream().map(r -> {
                String rs = "";
                final LeakType ip = LeakType.valueOfType(r);
                switch (ip) {
                    case LeakType.EMAIL:
                        rs = Labels.getLabels(LabelKey.LABEL_EMAIL);
                        break;
                    case LeakType.PHONE:
                        rs = Labels.getLabels(LabelKey.LABEL_PHONE_NUMBER);
                        break;
                    case LeakType.ADDRESS:
                        rs = Labels.getLabels(LabelKey.LABEL_ADDRESS);
                        break;
                    case LeakType.BANK_ACCOUNT:
                        rs = Labels.getLabels(LabelKey.LABEL_BANK_ACCOUNT);
                        break;
                    case LeakType.IDENTIFICATION:
                        rs = Labels.getLabels(LabelKey.LABEL_IDENTIFICATION);
                        break;
                    case null:
                        break;

                }
                return rs;
            }).toList() , ", ");

            List<Record> records = new ArrayList<>();
            response.getSources().stream().filter(Validator::isNotNull).forEach(item -> {

                String leakId = Md5Util.md5Hex(item);

                if (!recordRepository.existsByCustomerKeyAndLeakId(dataItem.getCustomerKey(), leakId)) {
                    Map<String, String> paramContent = new HashMap<>();
                    paramContent.put("TYPE", typeNom);
                    paramContent.put("LEAK_NAME", item.getName());
                    Long epoch = null;
                    if (Validator.isNotNull(item.getDate())) {
                        YearMonth ym = YearMonth.parse(item.getDate());
                        epoch = ym.atDay(1)
                                .atStartOfDay(zone)
                                .toInstant()
                                .getEpochSecond();
                    } else {
                        epoch = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
                    }

                    String description = StringUtil.replaceMapValue(contentTemplate.getContent(), paramContent);

                    Map<String, Object> meta = new HashMap<>();
                    meta.put("fields", response.getFields());

                    Record record = new Record(dataItem.getCustomerKey() , UUID.randomUUID().toString(), leakId, dataSource,
                            item.getName(), epoch,
                            epoch,
                            null,
                            meta,
                            description,
                            types
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

    List<String> resolveType(List<String> fields) {
        if (Validator.isNotNull(fields)) {
            return PiiScanner.convertToEntityAttribute(new HashSet<>(fields));
        }

        return List.of();
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
}
