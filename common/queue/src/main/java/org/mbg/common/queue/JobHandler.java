package org.mbg.common.queue;

@FunctionalInterface
public interface JobHandler<T> {
    void handle(String api, String token, String dataSource,T item) throws Exception;
}
