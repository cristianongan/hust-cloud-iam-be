package org.mbg.anm.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "api-consumer")
public class ApiConsumerProperties {

    private List<GroupIBAccount>  groupIbAccounts;

    @Data
    public static class GroupIBAccount {
        private String user;

        private String key;
    }
}
