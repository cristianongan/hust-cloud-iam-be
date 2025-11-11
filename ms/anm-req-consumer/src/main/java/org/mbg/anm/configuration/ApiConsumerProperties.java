package org.mbg.anm.configuration;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.mbg.common.queue.RedisQueueFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;

import java.util.List;

@Data
@Configuration
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "api-consumer")
public class ApiConsumerProperties {

    private List<Group> groupIbs;

    private List<Group> leakChecks;

    @Data
    public static class Group {
        private List<Account>  accounts;

        private String api;

        private String topic;

        private String dataSource;

        private String groupName;
    }

    @Data
    public static class Account {
        private String user;

        private String key;
    }
}
