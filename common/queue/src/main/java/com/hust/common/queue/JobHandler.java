package com.hust.common.queue;

@FunctionalInterface
public interface JobHandler<T> {
    void handle(T item) throws Exception;
}
