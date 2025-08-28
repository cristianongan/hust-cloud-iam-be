package org.mbg.common.cache;

import com.hazelcast.config.Config;
import com.hazelcast.config.ManagementCenterConfig;
import com.hazelcast.config.MapConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.mbg.common.cache.util.CacheConstants;
import org.mbg.common.util.EnvConstants;
import org.mbg.common.util.Validator;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;

import java.util.Map;
import java.util.Objects;

@Slf4j
@Getter
@Setter
@Configuration(value = "cacheHazelcastConfiguration")
@ConfigurationProperties(prefix = "cache.hazelcast")
@EnableCaching
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "cache", name = "config-type", havingValue = "hazelcast", matchIfMissing = false)
public class CacheHazelcastConfiguration implements DisposableBean {

    private String localIp;

    private String remoteIp;

    private int backupCount;

    private String instanceName;
    
    private ManagementCenter managementCenter;
    
    @Getter
    @Setter
    @NoArgsConstructor
    public static class ManagementCenter {
        private boolean enabled;
        
        private int updateInterval;
        
        private String url;
    }
    
	private final Environment env;

	private final CacheProperties properties;
	
	@Override
	public void destroy() throws Exception {
		_log.info("Closing Cache Manager");

		Hazelcast.shutdownAll();
	}

	@Bean
	public CacheManager cacheManager(HazelcastInstance hazelcastInstance) {
		_log.info("Starting HazelcastCacheManager");

		return new com.hazelcast.spring.cache.HazelcastCacheManager(hazelcastInstance);
	}

	@Bean
	public HazelcastInstance hazelcastInstance() {
		_log.info("Configuring Hazelcast");

		HazelcastInstance hazelCastInstance = Hazelcast.getHazelcastInstanceByName(this.getInstanceName());

		if (hazelCastInstance != null) {
			_log.info("Hazelcast already initialized");

			return hazelCastInstance;
		}

		Map<String, Integer> timeToLives = this.properties.getTimeToLives();

		Config config = new Config();

		config.setInstanceName(this.getInstanceName());
		config.getNetworkConfig().setPort(5704);
		config.getNetworkConfig().setPortAutoIncrement(true);
		// In development, remove multicast auto-configuration
		System.setProperty("hazelcast.local.localAddress", this.getLocalIp());

		if (env.acceptsProfiles(Profiles.of(EnvConstants.Profile.DEVELOPMENT))) {
			// System.setProperty("hazelcast.local.localAddress", properties.getIp());
			config.getNetworkConfig().getJoin().getAwsConfig().setEnabled(false);
			config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);
			config.getNetworkConfig().getJoin().getTcpIpConfig().setEnabled(true).addMember(this.getRemoteIp());
		} else {
			config.getNetworkConfig().getJoin().getAwsConfig().setEnabled(false);
			config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);
			config.getNetworkConfig().getJoin().getTcpIpConfig().setEnabled(false);
		}

		// Full reference is available at: http://docs.hazelcast.org/docs/management-center/3
		// .9/manual/html/Deploying_and_Starting.html
		config.setManagementCenterConfig(initializeDefaultManagementCenterConfig());

		// init map config
		for (Map.Entry<String, Integer> entry : timeToLives.entrySet()) {
			String key = entry.getKey();
			Integer timeToLive = entry.getValue();

			_log.info("Add cache with name {} and time to live {} seconds", key, timeToLive);
			
			config.getMapConfigs().put(key,
					Validator.equals(key, CacheConstants.Others.DEFAULT) ? initializeDefaultMapConfig(timeToLive)
							: initializeMapConfig(timeToLive));

		}

		return Hazelcast.newHazelcastInstance(config);
	}

	private ManagementCenterConfig initializeDefaultManagementCenterConfig() {
		return new ManagementCenterConfig();
	}

	private MapConfig initializeDefaultMapConfig(Integer timeToLive) {
		MapConfig mapConfig = new MapConfig();
		/*
		 * Number of backups. If 1 is set as the backup-count for example, then all entries of the map will
		 * be copied to another JVM for fail-safety. Valid numbers are 0 (no backup), 1, 2, 3.
		 */
		mapConfig.setBackupCount(this.getBackupCount());
		/*
		 * Valid values are: NONE (no eviction), LRU (Least Recently Used), LFU (Least Frequently Used).
		 * NONE is the default.
		 */
//		mapConfig.setEvictionPolicy(EvictionPolicy.LRU);
		/*
		 * Maximum size of the map. When max size is reached, map is evicted based on the policy defined.
		 * Any integer between 0 and Integer.MAX_VALUE. 0 means Integer.MAX_VALUE. Default is 0.
		 */
//		mapConfig.setMaxSizeConfig(new MaxSizeConfig(0, MaxSizeConfig.MaxSizePolicy.USED_HEAP_SIZE));

		if (Objects.nonNull(timeToLive)) {
			mapConfig.setTimeToLiveSeconds(timeToLive);
		}

		return mapConfig;
	}

	private MapConfig initializeMapConfig(Integer timeToLive) {
		MapConfig mapConfig = new MapConfig();

		if (Objects.nonNull(timeToLive)) {
			mapConfig.setTimeToLiveSeconds(timeToLive);
		}

		return mapConfig;
	}
}
