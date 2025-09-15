package org.mbg.anm.configuration;

import lombok.Data;
import org.mbg.anm.util.GatewayConstant;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = GatewayConstant.PropertyPrefix.CLIENT_CONFIG)
public class ClientApiProperties {
    private String verifyToken;
}
