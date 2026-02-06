package com.hust.common.queue;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.*;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public abstract class AbstractMessageProducer<T> {
    private final RedisTemplate<String, Object> redis;

    private final String streamKey;

    protected String stream() { return streamKey; }

    public void publish(T payload) {
        ObjectRecord<String, T> rec = StreamRecords
                .newRecord()
                .in(streamKey)
                .ofObject(payload);
        redis.opsForStream().add(rec);
    }

    @SuppressWarnings("unchecked")
    public List<RecordId> publishBatch(List<T> messages) {
        if (messages == null || messages.isEmpty()) return List.of();

        List<ObjectRecord<String, T>> records = messages.stream()
                .map(e -> StreamRecords.<String, T>newRecord().in(streamKey).ofObject(e))
                .toList();

        List<Object> out = redis.executePipelined((SessionCallback<Object>) new SessionCallback<>() {
            @Override public Object execute(RedisOperations ops) {
                StreamOperations<String, Object, Object> streamOps =
                        (StreamOperations<String, Object, Object>) ops.opsForStream();

                for (ObjectRecord<String, T> r : records) {
                    streamOps.add((ObjectRecord<String, Object>) (Object) r);
                }
                return null;
            }
        });

        return out.stream().map(o -> (RecordId) o).toList();
    }
}
