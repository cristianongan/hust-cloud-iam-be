package org.mbg.anm.queue;

import org.mbg.common.model.RedisMessage;
import org.mbg.common.queue.AbstractMessageListener;
import org.mbg.common.queue.JobHandler;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.redis.core.RedisTemplate;

public class LookUpDataMessageListener extends AbstractMessageListener<RedisMessage> {

    public LookUpDataMessageListener(
                                     JobHandler<RedisMessage> jobHandler, RedisTemplate<String, Object> redisTemplate,
                                     TaskExecutor taskExecutor, String consumer, String group, String topicName) {
        super(RedisMessage.class, jobHandler, redisTemplate, taskExecutor, consumer, group, topicName);
    }
}
