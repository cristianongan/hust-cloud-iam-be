package org.mbg.common.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class RedisMessage implements Message, Serializable {
    @Serial
    private static final long serialVersionUID = 2227371983973955460L;

    private String id;

    private String payload;

    private String topic;

    private int priority;

    private Long createdAt;

    public static RedisMessage of(String payload, String topic, int priority) {
        return RedisMessage.builder()
                .id(UUID.randomUUID().toString())
                .payload(payload)
                .topic(topic)
                .priority(priority)
                .createdAt(System.currentTimeMillis())
                .build();
    }

}
