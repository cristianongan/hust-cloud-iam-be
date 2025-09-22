package org.mbg.common.queue;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mbg.common.model.RedisMessage;
import org.mbg.common.util.Validator;
import org.redisson.api.*;
import org.redisson.client.RedisClient;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class Producer {
    private final RedisQueueFactory redisQueueFactory;

    public boolean sendPriorityMessage(RedisMessage message) {
        _log.info("Producer sendPriorityMessage topic: {} - message: {}", message.getTopic(), message.getPayload());
        if (Validator.isNull(message) || Validator.isNull(message.getTopic()) || Validator.isNull(message.getPayload())) {
            return false;
        }

        RPriorityBlockingQueue<RedisMessage> q = this.redisQueueFactory.getPriorityTopic(message.getTopic());

        if (q == null) {
            return false;
        }

        q.add(message);

        return true;
    }

    public boolean sendMessage(RedisMessage message) {
        if (Validator.isNull(message) || Validator.isNull(message.getTopic()) || Validator.isNull(message.getPayload())) {
            return false;
        }

        RQueue<RedisMessage> q = this.redisQueueFactory.getNormalTopic(message.getTopic());

        if (Validator.isNull(q)) {
            return false;
        }

        q.add(message);

        return true;
    }


}
