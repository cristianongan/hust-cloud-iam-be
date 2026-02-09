package com.hust.service;

import com.hust.api.response.SmsResponse;

import java.util.Map;

public interface EmailService {

    void send(String email, String code, Map<String, String> params);
}
