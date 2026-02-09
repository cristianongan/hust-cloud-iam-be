/*
 * SmsProperties.java
 *
 * Copyright (C) 2021 by Evotek. All right reserved.
 * This software is the confidential and proprietary information of Evotek
 */
package com.hust.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import lombok.Data;

/**
 * 11/11/2021 - LinhLH: Create new
 *
 * @author LinhLH
 */
@Data
@Component
@ConfigurationProperties(prefix = "sms")
public class SmsProperties {

    private String user;
    
    private String password;
    
    private String cpCode;
    
    private String requestId;
    
    private String serviceId;
    
    private String schema;
    
    private String url;
    
    private String commandCode;
    
    private String contentType;
}
