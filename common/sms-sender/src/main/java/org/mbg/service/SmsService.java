package com.hust.service;

import java.util.Map;
import com.hust.api.request.SmsRequest;
import com.hust.api.response.SmsResponse;

public interface SmsService {
    SmsResponse send(SmsRequest request);

    SmsResponse send(String phoneNumber, String code, Map<String, String> params);
}
