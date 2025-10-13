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

    private Object payload;

    private String topic;

    private int priority;

    private Long createdAt;

    public static RedisMessage of(Object payload) {
        return RedisMessage.builder()
                .id(UUID.randomUUID().toString())
                .payload(payload)
                .createdAt(System.currentTimeMillis())
                .build();
    }

}
