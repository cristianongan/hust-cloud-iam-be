package org.mbg.common.queue;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mbg.common.model.RedisMessage;
//import org.redisson.api.RPriorityBlockingQueue;
import org.mbg.common.util.Validator;
import org.springframework.context.SmartLifecycle;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractMessageListener<T> implements SmartLifecycle {

    private final Class<T> payloadType;

    private final JobHandler<T> jobHandler;

    private final RedisTemplate<String, Object> redisTemplate;

    private final TaskExecutor taskExecutor;

    private final String consumer;

    private final String group;

    private final String topicName;

    private boolean running;

    private boolean ackOnFail = true;

    @Override
    public void start() {
        running = true;

        ensureGroup();
        Consumer c = Consumer.from(group, consumer);

        taskExecutor.execute(() -> {
            while (running) {
                try {
                    var opts = StreamReadOptions.empty()
                            .count(1)
                            .block(Duration.ofMillis(200));

                    List<ObjectRecord<String, T>> messages =
                            redisTemplate.opsForStream().read(payloadType, c, opts,
                                    StreamOffset.create(topicName, ReadOffset.lastConsumed()));

                    if (messages == null || messages.isEmpty()) continue;

                    for (ObjectRecord<String, T> rec : messages) {
                        var id = rec.getId();
                        T value = rec.getValue();

                        this.taskExecutor.execute(() -> {
                            Long start = System.currentTimeMillis();
                            _log.info(" start handle  message {} on stream {} ", id, topicName);

                            try {
                                this.jobHandler.handle(value);
                            } catch (Exception ex) {
                                _log.error("[stream] handle fail id={}: {}", id, ex.getMessage(), ex);
                                redisTemplate.opsForStream().add(StreamRecords.newRecord()
                                        .in(topicName + ":DLQ")
                                        .ofMap(Map.of(
                                                "msgId", id.getValue(),
                                                "payload", value.toString(),
                                                "error", ex.getClass().getSimpleName() + ": " + ex.getMessage()
                                        )));
                                if (ackOnFail) {
                                    redisTemplate.opsForStream().acknowledge(topicName, group, id);
                                }
                            }

                            Long end = System.currentTimeMillis();
                            _log.info(" finish handle  message {} on stream {}  with duration {} ms", id, topicName, end - start);
                        });
                    }
                } catch (Exception e) {
                    _log.info("[stream] poll error: {}", e.toString());
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException ignored) {
                    }
                }
            }
        });
    }

    protected void configure(boolean ackOnFail) {
        this.ackOnFail = ackOnFail;
    }

    private void ensureGroup() {
        try {
            redisTemplate.opsForStream().createGroup(topicName, ReadOffset.latest(), group);
            _log.info("[stream] group created: {} -> {}", topicName, group);
        } catch (Exception ignore) { /* exists */ }
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
