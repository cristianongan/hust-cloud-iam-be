package org.mbg.common.queue;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
//import org.redisson.api.RDelayedQueue;
//import org.redisson.api.RPriorityBlockingQueue;
//import org.redisson.api.RQueue;
//import org.redisson.api.RedissonClient;


@Slf4j
//@Configuration
@RequiredArgsConstructor
//@ConditionalOnProperty(prefix = "cache", name = "config-type", havingValue = "redisson")
public class RedisQueueFactory {
//    private final RedissonClient redissonClient;
//
//    private final RedisQueueProperties redisQueueProperties;
//
//    private final Map<String, RPriorityBlockingQueue<RedisMessage>> priorityQueueMap = new HashMap<>();
//    private final Map<String, RQueue<RedisMessage>> normalQueueMap = new HashMap<>();

    @PostConstruct
    public void init() {
//        _log.info("RedisQueueFactory init! ");
//        if (redissonClient == null) {
//            return;
//        }
//
//        if (Validator.isNotNull(redisQueueProperties.getPriorityTopics())) {
//            RedisMessageComparator comparator = new RedisMessageComparator();
//            for (String topic : redisQueueProperties.getPriorityTopics()) {
//                RPriorityBlockingQueue<RedisMessage> q = redissonClient.getPriorityBlockingQueue(topic);
//                q.trySetComparator(comparator);
//
//                priorityQueueMap.put(topic, q);
//            }
//        }
//
//        if (Validator.isNotNull(redisQueueProperties.getNormalTopics())) {
//            for (String topic : redisQueueProperties.getPriorityTopics()) {
//                RQueue<RedisMessage> q = redissonClient.getQueue(topic);
//
//                normalQueueMap.put(topic, q);
//            }
//        }
    }

//    public RPriorityBlockingQueue<RedisMessage> getPriorityTopic(String topic) {
//        if (this.priorityQueueMap.containsKey(topic)) {
//            return this.priorityQueueMap.get(topic);
//        }
//
//        return null;
//    }
//
//    public RQueue<RedisMessage> getNormalTopic(String topic) {
//        if (this.normalQueueMap.containsKey(topic)) {
//            return this.normalQueueMap.get(topic);
//        }
//
//        return null;
//    }
}
