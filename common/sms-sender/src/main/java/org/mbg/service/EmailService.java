package org.mbg.service;

import org.mbg.api.response.SmsResponse;

import java.util.Map;

public interface EmailService {

    void send(String email, String code, Map<String, String> params);
}
