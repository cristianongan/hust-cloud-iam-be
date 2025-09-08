package org.mbg.common.model;

import lombok.Data;

import java.time.LocalDateTime;

public interface Message {
    String getId();

    String getPayload();

    int getPriority();

    Long getCreatedAt();

    String getTopic();
}
