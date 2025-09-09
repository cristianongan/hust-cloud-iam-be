package org.mbg.common.queue;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mbg.common.model.RedisMessage;
import org.mbg.common.util.Validator;
import org.redisson.api.RPriorityBlockingQueue;
import org.redisson.api.RQueue;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class Producer {
    private final RedisQueueFactory redisQueueFactory;

    public boolean sendPriorityMessage(RedisMessage message) {
        if (Validator.isNull(message) || Validator.isNull(message.getTopic()) || Validator.isNull(message.getPayload())) {
            return false;
        }

        RPriorityBlockingQueue<RedisMessage> q = this.redisQueueFactory.getPriorityTopic(message.getTopic());

        if (Validator.isNull(q)) {
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
