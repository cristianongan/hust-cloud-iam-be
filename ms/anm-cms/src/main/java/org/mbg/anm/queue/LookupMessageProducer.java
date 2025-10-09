package org.mbg.anm.queue;

import lombok.extern.slf4j.Slf4j;
import org.mbg.anm.configuration.MessageListenerProperties;
import org.mbg.common.queue.AbstractMessageProducer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LookupMessageProducer extends AbstractMessageProducer<Long> {

    public LookupMessageProducer(RedisTemplate<String, Object> redis, MessageListenerProperties properties) {
        super(redis, properties.getLookupTopicName());
    }
}
