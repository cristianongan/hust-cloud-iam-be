package com.hust.iam.configuration;

import lombok.Data;
import com.hust.iam.util.GatewayConstant;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = GatewayConstant.PropertyPrefix.CLIENT_CONFIG)
public class ClientApiProperties {
    private String verifyToken;
}
