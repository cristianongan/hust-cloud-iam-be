package org.mbg.service;

import java.util.Map;
import org.mbg.api.request.SmsRequest;
import org.mbg.api.response.SmsResponse;

public interface SmsService {
    SmsResponse send(SmsRequest request);

    SmsResponse send(String phoneNumber, String code, Map<String, String> params);
}
