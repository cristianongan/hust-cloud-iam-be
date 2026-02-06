package com.hust.common.comparator;

import com.hust.common.model.RedisMessage;

import java.util.Comparator;

public class RedisMessageComparator implements Comparator<RedisMessage> {
    @Override
    public int compare(RedisMessage o1, RedisMessage o2) {
        int c = Integer.compare(o1.getPriority(), o2.getPriority());
        if (c != 0) return c;

        return Long.compare(o1.getCreatedAt(), o2.getCreatedAt());
    }
}
