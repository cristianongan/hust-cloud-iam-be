package org.mbg.anm.queue;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mbg.common.model.RedisMessage;
import org.mbg.common.queue.JobHandler;
import org.mbg.common.queue.RedisQueueFactory;
import org.mbg.common.util.Validator;
//import org.redisson.api.RPriorityBlockingQueue;
import org.springframework.context.SmartLifecycle;
import org.springframework.core.task.TaskExecutor;

import java.util.function.Function;
import java.util.function.Supplier;

@Slf4j
@RequiredArgsConstructor
public class RedisPriorityMessageWorker implements SmartLifecycle {

    private final JobHandler<RedisMessage> jobHandler;

    private final RedisQueueFactory redisQueueFactory;

    private final TaskExecutor taskExecutor;

    private final String topicName;

    private boolean running;

    private final Supplier<String> getToken;

    private final String dataSource;

    private final String api;

    @Override
    public void start() {
//        running = true;
//
////        RPriorityBlockingQueue<RedisMessage> queue = this.redisQueueFactory.getPriorityTopic(topicName);
//
//        if (queue == null) {
//            throw new RuntimeException("queue not found: " + topicName);
//        }
//
//        taskExecutor.execute(() -> {
//            _log.info("RedisPriorityMessageWorker start listener : {}", topicName);
//
//            while (running) {
//                RedisMessage data = queue.poll();
//
//                if (Validator.isNotNull(data)) {
//                    try {
//                        jobHandler.handle(api, getToken.get(), dataSource,data);
//                    } catch (Exception e) {
//                        _log.error("RedisPriorityMessageWorker handle data occurred an exception: {}", e.getMessage());
//                    }
//                }
//
//                try {
//                    java.util.concurrent.TimeUnit.SECONDS.sleep(1);
//                } catch (InterruptedException e) {
//                    Thread.currentThread().interrupt();
//                    return;
//                }
//            }
//
//            _log.info("RedisPriorityMessageWorker stop listener : {}", topicName);
//        });
    }

    @Override
    public void stop() {
        running = false;
    }

    @Override
    public boolean isRunning() {
        return running;
    }
}
