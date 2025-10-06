//package org.mbg.common.cache;
//
//import org.mbg.common.util.StringPool;
//import org.mbg.common.util.StringUtil;
//import org.mbg.common.util.Validator;
//import lombok.Getter;
//import lombok.RequiredArgsConstructor;
//import lombok.Setter;
//import org.redisson.Redisson;
//import org.redisson.api.NameMapper;
//import org.redisson.api.RedissonClient;
//import org.redisson.client.FailedConnectionDetector;
//import org.redisson.config.ClusterServersConfig;
//import org.redisson.config.Config;
//import org.redisson.config.ReadMode;
//import org.redisson.config.ReplicatedServersConfig;
//import org.redisson.config.SentinelServersConfig;
//import org.redisson.config.SingleServerConfig;
//import org.redisson.config.SslVerificationMode;
//import org.redisson.config.SubscriptionMode;
//import org.redisson.config.TransportMode;
//import org.redisson.spring.cache.CacheConfig;
//import org.redisson.spring.cache.RedissonSpringCacheManager;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.cache.CacheManager;
//import org.springframework.cache.annotation.EnableCaching;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Objects;
//
///**
// * @author LinhLH
// *
// */
//@Getter
//@Setter
//@Configuration
//@ConfigurationProperties(prefix = "cache.redisson")
//@EnableCaching
//@RequiredArgsConstructor
//@ConditionalOnProperty(prefix = "cache", name = "config-type", havingValue = "redisson")
//public class CacheRedissonConfiguration {
//	private String mode;
//
//	private int cacheDuration;
//
//	private String clientName;
//
//	private String password;
//
//	private int subscriptionsPerConnection;
//
//	private int idleConnectionTimeout;
//
//	private int connectTimeout;
//
//	private int timeout;
//
//	private int retryAttempts;
//
//	private int retryInterval;
//
//	private int threads;
//
//	private int nettyThreads;
//
//	private String transportMode;
//
//	private int database;
//
//	private String keyPrefix;
//
//	private Single single;
//
//	private Sentinel sentinel;
//
//	private Cluster cluster;
//
//	private Cluster replicated;
//
//	@Getter
//	@Setter
//	public static class Single {
//		private String address;
//
//		private int subscriptionConnectionMinimumIdleSize;
//
//		private int subscriptionConnectionPoolSize;
//
//		private int connectionMinimumIdleSize;
//
//		private int connectionPoolSize;
//
//		private long dnsMonitoringInterval;
//	}
//
//	@Getter
//	@Setter
//	public static class Sentinel {
//		private int failedSlaveReconnectionInterval;
//
//		private int failedSlaveCheckInterval;
//
//		private int subscriptionConnectionMinimumIdleSize;
//
//		private int subscriptionConnectionPoolSize;
//
//		private int slaveConnectionMinimumIdleSize;
//
//		private int slaveConnectionPoolSize;
//
//		private int masterConnectionMinimumIdleSize;
//
//		private int masterConnectionPoolSize;
//
//		private String readMode;
//
//		private int pingConnectionInterval;
//
//		private String subscriptionMode;
//
//		private String[] nodes;
//
//		private String masterName;
//
//		private boolean checkSentinelsList;
//	}
//
//	@Getter
//    @Setter
//    public static class Cluster {
//	    private int scanInterval;
//
//	    private String[] nodes;
//
//	    private int failedSlaveReconnectionInterval;
//
//        private int failedSlaveCheckInterval;
//
//        private int subscriptionConnectionMinimumIdleSize;
//
//        private int subscriptionConnectionPoolSize;
//
//        private int slaveConnectionMinimumIdleSize;
//
//        private int slaveConnectionPoolSize;
//
//        private int masterConnectionMinimumIdleSize;
//
//        private int masterConnectionPoolSize;
//
//        private String readMode;
//
//        private int pingConnectionInterval;
//
//        private String subscriptionMode;
//	}
//
//	public enum Mode {
//		SINGLE, SENTINEL, CLUSTER, REPLICATED
//	}
//
//	private final CacheProperties properties;
//
//	@Bean
//	public Config createConfig() {
//		Config config = new Config();
//
//		// common config
//
//		config.setNettyThreads(this.nettyThreads);
//		config.setThreads(this.threads);
//		config.setTransportMode(TransportMode.valueOf(this.transportMode));
//
//		// sentinel
//		if (Validator.equals(this.mode, Mode.SENTINEL.name().toLowerCase())) {
//			SentinelServersConfig sentinelConfig = config.useSentinelServers();
//
//			// common config
//			sentinelConfig.setClientName(this.clientName);
//
//			if (Objects.nonNull(this.password)) {
//				sentinelConfig.setPassword(this.password);
//			}
//
//			sentinelConfig.setSubscriptionsPerConnection(this.subscriptionsPerConnection);
//			sentinelConfig.setDatabase(this.database);
//			sentinelConfig.setRetryAttempts(this.retryAttempts);
//			sentinelConfig.setRetryInterval(this.retryInterval);
//			sentinelConfig.setConnectTimeout(this.connectTimeout);
//			sentinelConfig.setTimeout(this.timeout);
//			sentinelConfig.setIdleConnectionTimeout(this.idleConnectionTimeout);
//
//			// special config
//			sentinelConfig.setMasterName(this.sentinel.getMasterName());
//			sentinelConfig.addSentinelAddress(this.sentinel.getNodes());
//			sentinelConfig.setFailedSlaveReconnectionInterval(this.sentinel.getFailedSlaveReconnectionInterval());
//			sentinelConfig.setFailedSlaveNodeDetector(new FailedConnectionDetector(this.sentinel.getFailedSlaveCheckInterval()));
//			sentinelConfig
//					.setSubscriptionConnectionMinimumIdleSize(this.sentinel.getSubscriptionConnectionMinimumIdleSize());
//			sentinelConfig.setSubscriptionConnectionPoolSize(this.sentinel.getSubscriptionConnectionPoolSize());
//			sentinelConfig.setSlaveConnectionMinimumIdleSize(this.sentinel.getSlaveConnectionMinimumIdleSize());
//			sentinelConfig.setSlaveConnectionPoolSize(this.sentinel.getSlaveConnectionPoolSize());
//			sentinelConfig.setMasterConnectionMinimumIdleSize(this.sentinel.getMasterConnectionMinimumIdleSize());
//			sentinelConfig.setMasterConnectionPoolSize(this.sentinel.getMasterConnectionPoolSize());
//			sentinelConfig.setPingConnectionInterval(this.sentinel.getPingConnectionInterval());
//			sentinelConfig.setReadMode(ReadMode.valueOf(this.sentinel.getReadMode()));
//			sentinelConfig.setSubscriptionMode(SubscriptionMode.valueOf(this.sentinel.getSubscriptionMode()));
//			sentinelConfig.setCheckSentinelsList(this.sentinel.isCheckSentinelsList());
//			sentinelConfig.setNameMapper(getNameMapper(this.keyPrefix));
//			sentinelConfig.setSslVerificationMode(SslVerificationMode.NONE);
//		} else if (Validator.equals(this.mode, Mode.SINGLE.name().toLowerCase())) { // single server
//			SingleServerConfig singleConfig = config.useSingleServer();
//
//			// common config
//			singleConfig.setClientName(this.clientName);
//
//			if (Objects.nonNull(this.password)) {
//				singleConfig.setPassword(this.password);
//			}
//
//			singleConfig.setSubscriptionsPerConnection(this.subscriptionsPerConnection);
//			singleConfig.setDatabase(this.database);
//			singleConfig.setRetryAttempts(this.retryAttempts);
//			singleConfig.setRetryInterval(this.retryInterval);
//			singleConfig.setConnectTimeout(this.connectTimeout);
//			singleConfig.setTimeout(this.timeout);
//			singleConfig.setIdleConnectionTimeout(this.idleConnectionTimeout);
//
//			// special config
//			// format as redis://127.0.0.1:7181 or rediss://127.0.0.1:7181 for SSL
//			singleConfig.setAddress(this.single.getAddress());
//			singleConfig.setSubscriptionConnectionMinimumIdleSize(this.single.getSubscriptionConnectionMinimumIdleSize());
//			singleConfig.setSubscriptionConnectionPoolSize(this.single.getSubscriptionConnectionPoolSize());
//			singleConfig.setConnectionMinimumIdleSize(this.single.getConnectionMinimumIdleSize());
//			singleConfig.setConnectionPoolSize(this.single.getConnectionPoolSize());
//			singleConfig.setDnsMonitoringInterval(this.single.getDnsMonitoringInterval());
//			singleConfig.setNameMapper(getNameMapper(this.keyPrefix));
//		} else if (Validator.equals(this.mode, Mode.CLUSTER.name().toLowerCase())) {
//			ClusterServersConfig clusterConfig =
//					config.useClusterServers();
//
//			// common config
//			clusterConfig.setClientName(this.clientName);
//
//			if (Objects.nonNull(this.password)) {
//				clusterConfig.setPassword(this.password);
//			}
//
//			clusterConfig.addNodeAddress(this.cluster.getNodes());
//
//			clusterConfig.setSubscriptionsPerConnection(this.subscriptionsPerConnection);
//			clusterConfig.setRetryAttempts(this.retryAttempts);
//			clusterConfig.setRetryInterval(this.retryInterval);
//			clusterConfig.setConnectTimeout(this.connectTimeout);
//			clusterConfig.setTimeout(this.timeout);
//			clusterConfig.setIdleConnectionTimeout(this.idleConnectionTimeout);
//
//			// special config
//			clusterConfig.setScanInterval(this.cluster.getScanInterval());
//			clusterConfig.setFailedSlaveReconnectionInterval(this.cluster.getFailedSlaveReconnectionInterval());
//			clusterConfig.setFailedSlaveNodeDetector(new FailedConnectionDetector(this.cluster.getFailedSlaveCheckInterval()));
//			clusterConfig
//					.setSubscriptionConnectionMinimumIdleSize(this.cluster.getSubscriptionConnectionMinimumIdleSize());
//			clusterConfig.setSubscriptionConnectionPoolSize(this.cluster.getSubscriptionConnectionPoolSize());
//			clusterConfig.setSlaveConnectionMinimumIdleSize(this.cluster.getSlaveConnectionMinimumIdleSize());
//			clusterConfig.setSlaveConnectionPoolSize(this.cluster.getSlaveConnectionPoolSize());
//			clusterConfig.setMasterConnectionMinimumIdleSize(this.cluster.getMasterConnectionMinimumIdleSize());
//			clusterConfig.setMasterConnectionPoolSize(this.cluster.getMasterConnectionPoolSize());
//			clusterConfig.setPingConnectionInterval(this.cluster.getPingConnectionInterval());
//			clusterConfig.setReadMode(ReadMode.valueOf(this.cluster.getReadMode()));
//			clusterConfig.setSubscriptionMode(SubscriptionMode.valueOf(this.cluster.getSubscriptionMode()));
//			clusterConfig.setNameMapper(getNameMapper(this.keyPrefix));
//		} else if (Validator.equals(this.mode, Mode.REPLICATED.name().toLowerCase())) {
//			ReplicatedServersConfig replicatedConfig = config.useReplicatedServers();
//
//			// common config
//			replicatedConfig.setClientName(this.clientName);
//
//			if (Objects.nonNull(this.password)) {
//				replicatedConfig.setPassword(this.password);
//			}
//
//			replicatedConfig.addNodeAddress(this.replicated.getNodes());
//
//			replicatedConfig.setSubscriptionsPerConnection(this.subscriptionsPerConnection);
//			replicatedConfig.setRetryAttempts(this.retryAttempts);
//			replicatedConfig.setRetryInterval(this.retryInterval);
//			replicatedConfig.setConnectTimeout(this.connectTimeout);
//			replicatedConfig.setTimeout(this.timeout);
//			replicatedConfig.setIdleConnectionTimeout(this.idleConnectionTimeout);
//
//			// special config
//			replicatedConfig.setFailedSlaveReconnectionInterval(this.replicated.getFailedSlaveReconnectionInterval());
//			replicatedConfig.setFailedSlaveNodeDetector(new FailedConnectionDetector(this.replicated.getFailedSlaveCheckInterval()));
//			replicatedConfig
//					.setSubscriptionConnectionMinimumIdleSize(this.replicated.getSubscriptionConnectionMinimumIdleSize());
//			replicatedConfig.setSubscriptionConnectionPoolSize(this.replicated.getSubscriptionConnectionPoolSize());
//			replicatedConfig.setSlaveConnectionMinimumIdleSize(this.replicated.getSlaveConnectionMinimumIdleSize());
//			replicatedConfig.setSlaveConnectionPoolSize(this.replicated.getSlaveConnectionPoolSize());
//			replicatedConfig.setMasterConnectionMinimumIdleSize(this.replicated.getMasterConnectionMinimumIdleSize());
//			replicatedConfig.setMasterConnectionPoolSize(this.replicated.getMasterConnectionPoolSize());
//			replicatedConfig.setPingConnectionInterval(this.replicated.getPingConnectionInterval());
//			replicatedConfig.setReadMode(ReadMode.valueOf(this.replicated.getReadMode()));
//			replicatedConfig.setSubscriptionMode(SubscriptionMode.valueOf(this.replicated.getSubscriptionMode()));
//			replicatedConfig.setNameMapper(getNameMapper(this.keyPrefix));
//			replicatedConfig.setSslVerificationMode(SslVerificationMode.NONE);
//		}
//
//		return config;
//	}
//
//	@Bean("redissonClient")
//	public RedissonClient redissonClient() {
//		return Redisson.create(createConfig());
//	}
//
//	@Bean
//	CacheManager cacheManager() {
//		Map<String, CacheConfig> config = new HashMap<>();
//
//		// define local cache settings for "default" cache.
//		// ttl = {cacheDuration} minutes and maxIdleTime = {cacheDuration/2} minutes for local cache entries
//        config.put("default", new CacheConfig((long) this.cacheDuration * 60 * 1000,
//				(long) this.cacheDuration * 60 * 1000 / 2));
//
//		Map<String, Integer> timeToLives = this.properties.getTimeToLives();
//
//		for (Map.Entry<String, Integer> entry : timeToLives.entrySet()) {
//            String key = entry.getKey();
//            Integer timeToLive = entry.getValue();
//
//            config.put(key, new CacheConfig(timeToLive * 1000, timeToLive * 1000 / 2));
//        }
//
//		return new RedissonSpringCacheManager(redissonClient(), config);
//	}
//
//	private NameMapper getNameMapper(String keyPrefix) {
//        if (Validator.isNull(keyPrefix)) {
//            return NameMapper.direct();
//        }
//
//        return new NameMapper() {
//            @Override
//            public String unmap(String name) {
//                return StringUtil.subAfter(name, keyPrefix + StringPool.COLON, false);
//            }
//
//            @Override
//            public String map(String name) {
//                return keyPrefix + StringPool.COLON + name;
//            }
//        };
//    }
//
//}
