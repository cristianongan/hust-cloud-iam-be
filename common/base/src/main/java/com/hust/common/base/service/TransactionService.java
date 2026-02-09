package com.hust.common.base.service;

import com.hust.common.security.util.SecurityConstants;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

public interface TransactionService {
    @CachePut(cacheNames = SecurityConstants.CACHE.TRANSACTION, key = "#transactionId", unless = "#result == null")
    default <T> T saveTransaction(String transactionId, T input) {
        return input;
    }

    @Cacheable(cacheNames = SecurityConstants.CACHE.TRANSACTION, key = "#transactionId", unless = "#result == null")
    default <T> T getTransaction(String transactionId, Class<T> clazz) {
        return null;
    }

    @CacheEvict(cacheNames = SecurityConstants.CACHE.TRANSACTION, key = "#transactionId")
    default void invalidate(String transactionId) {}
}
