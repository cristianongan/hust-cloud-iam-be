package org.mbg.anm.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "message-listener")
public class MessageListenerProperties {

    private Integer lookupMessageLimit;

    private String lookupTopicName;
}
