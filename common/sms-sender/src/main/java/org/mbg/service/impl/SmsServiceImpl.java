package com.hust.service.impl;

import java.util.Map;
import java.util.UUID;

import com.hust.common.base.enums.EntityStatus;
import com.hust.common.base.model.ContentTemplate;
import com.hust.common.base.repository.ContentTemplateRepository;
import com.hust.common.label.LabelKey;
import com.hust.common.label.Labels;
import com.hust.common.util.StringUtil;
import com.hust.common.util.Validator;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import com.hust.api.consumer.SmsSender;
import com.hust.api.request.SmsRequest;
import com.hust.api.response.SmsResponse;
import com.hust.api.response.SmsResponse.Result;
import com.hust.configuration.SmsProperties;
import com.hust.service.SmsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmsServiceImpl implements SmsService {
    
    private final ContentTemplateRepository contentTemplateRepository;
    
    private final SmsProperties properties;

    private final SmsSender smsSender;
    
    @Override
    public SmsResponse send(SmsRequest request) {
        SmsResponse smsResponse = this.smsSender.send(request);

        if (!Validator.equals(smsResponse.getResult(), SmsResponse.Result.SUCCESS.getStatus())) {
            _log.error("Sms fail to send to phone number {} : {}", request.getTo(), smsResponse.getMessage());
        }
        
        return smsResponse;
    }

    @Override
    public SmsResponse send(String phoneNumber, String code, Map<String, String> params) {
        ContentTemplate template =
                        this.contentTemplateRepository.findByTemplateCodeAndStatus(code,
                                        EntityStatus.ACTIVE.getStatus());

        if (Validator.isNull(template)) {
            _log.error("Cannot find content template with code {}", code);

            return new SmsResponse(Result.FAILURE, Labels.getLabels(LabelKey.ERROR_DATA_DOES_NOT_EXIST,
                            new Object[] {Labels.getLabels(LabelKey.LABEL_TEMPLATE)}));
        }

        SmsRequest request = SmsRequest.builder()
                        .clientMessageId(UUID.randomUUID().toString())
                        .method(HttpMethod.POST)
                        .baseUrl(this.properties.getUrl())
                        .to(phoneNumber)
                        .title(template.getTitle())
                        .content(StringUtil.replaceMapValue(template.getContent(), params))
                        .build();

        if (_log.isDebugEnabled()) {
            _log.debug("Sms content: {}", request.getContent());
        }

        return this.send(request);
    }

}
