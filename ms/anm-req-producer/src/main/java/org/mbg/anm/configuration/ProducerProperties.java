package org.mbg.anm.configuration;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "producer")
public class ProducerProperties {
    private int subscribeBatchLimit;

    private int customerDataExtendLimit;
}
