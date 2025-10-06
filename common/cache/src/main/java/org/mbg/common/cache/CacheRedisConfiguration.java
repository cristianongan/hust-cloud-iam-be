package org.mbg.common.cache;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import org.mbg.common.cache.util.CacheConstants;
import org.mbg.common.util.Validator;
import io.lettuce.core.ClientOptions;
import io.lettuce.core.resource.DefaultClientResources;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.lang.NonNull;
import org.zalando.problem.jackson.ProblemModule;
import org.zalando.problem.violations.ConstraintViolationProblemModule;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Getter
@Setter
@Configuration(value = "cacheRedisConfiguration")
@EnableCaching
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "cache", name = "config-type", havingValue = "redis", matchIfMissing = true)
public class CacheRedisConfiguration implements CachingConfigurer {
	
	private final CacheProperties properties;

	public String getMode() {
		return this.properties.getRedis().getMode();
	}
	
	@Bean
	public RedisConnectionFactory redisConnectionFactory() {

		LettuceConnectionFactory lettuceConnectionFactory;

		GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();

		poolConfig.setMaxIdle(this.properties.getRedis().getLettucePool().getMaxIdle());
        poolConfig.setMinIdle(this.properties.getRedis().getLettucePool().getMinIdle());
        poolConfig.setMaxWait(Duration.ofMillis(this.properties.getRedis().getLettucePool().getMaxWaitMillis()));
        poolConfig.setMaxTotal(this.properties.getRedis().getLettucePool().getMaxTotal());
		
        // @formatter:off
        ClientOptions clientOptions = ClientOptions.builder()
        		.disconnectedBehavior(ClientOptions.DisconnectedBehavior.REJECT_COMMANDS)
                .autoReconnect(true)
                .build();
        
		LettucePoolingClientConfiguration lettuceConfiguration = LettucePoolingClientConfiguration.builder()
				.commandTimeout(Duration.ofMillis(this.properties.getRedis().getLettucePool().getCommandTimeout()))
				.shutdownTimeout(Duration.ofMillis(this.properties.getRedis().getLettucePool().getShutdownTimeout()))
				.clientOptions(clientOptions)
				.clientResources(DefaultClientResources.create())
				.poolConfig(poolConfig)
				.build();
		// @formatter:on
		
		if (Validator.equals(this.getMode(), CacheProperties.Mode.STANDALONE.name().toLowerCase())) {
			RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();

			redisStandaloneConfiguration.setHostName(this.properties.getRedis().getStandalone().getHost());
			redisStandaloneConfiguration.setPort(this.properties.getRedis().getStandalone().getPort());
			redisStandaloneConfiguration.setPassword(RedisPassword.of(this.properties.getRedis().getStandalone().getPassword()));

			lettuceConnectionFactory =
					new LettuceConnectionFactory(redisStandaloneConfiguration, lettuceConfiguration);
		} else {
			RedisSentinelConfiguration sentinelConfig =
					new RedisSentinelConfiguration().master(this.properties.getRedis().getSentinel().getMaster());

			this.properties.getRedis().getSentinel().getNodes().forEach(s ->
					sentinelConfig.sentinel(s, this.properties.getRedis().getSentinel().getPort()));

			if (Objects.nonNull(this.properties.getRedis().getSentinel().getPassword())) {
				sentinelConfig.setPassword(this.properties.getRedis().getSentinel().getPassword());
				sentinelConfig.setSentinelPassword(this.properties.getRedis().getSentinel().getPassword());
			}

			lettuceConnectionFactory = new LettuceConnectionFactory(sentinelConfig, lettuceConfiguration);

		}

		return lettuceConnectionFactory;
	}

	@Bean("redisTemplate")
	public RedisTemplate<String, String> redisTemplate() {
		_log.info("start create redisTemplate");
		final StringRedisTemplate template = new StringRedisTemplate(redisConnectionFactory());

		ObjectMapper om = this.createObjectMapper();

		Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer =
				new Jackson2JsonRedisSerializer<>(om, Object.class);

		RedisSerializer<String> stringSerializer = new StringRedisSerializer();

		template.setKeySerializer(stringSerializer);
		template.setValueSerializer(jackson2JsonRedisSerializer);

		template.setHashKeySerializer(stringSerializer);
		template.setHashValueSerializer(jackson2JsonRedisSerializer);
		
		template.setDefaultSerializer(stringSerializer);

		return template;
	}

	@Bean
    public RedisCacheConfiguration cacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(this.properties.getRedis().getCacheDuration()))
                        .disableCachingNullValues()
                        .serializeValuesWith(SerializationPair
                                        .fromSerializer(new GenericJackson2JsonRedisSerializer(createObjectMapper())));
    }

	@Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
        Map<String, Integer> timeTolives = this.properties.getTimeToLives();

        return builder -> {
        	Map<String, RedisCacheConfiguration> configurationMap = new HashMap<>();
        	
            for (Map.Entry<String, Integer> entry : timeTolives.entrySet()) {
                String key = entry.getKey();
                Integer timeTolive = entry.getValue();

				configurationMap.put(key,
						RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(timeTolive)));
            }

			configurationMap.put(CacheConstants.Others.DEFAULT,
					RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(
							this.properties.getRedis().getCacheDuration())));
            
            builder.withInitialCacheConfigurations(configurationMap);
        };
    }

    private ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper()
				.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        PolymorphicTypeValidator ptv =
                        BasicPolymorphicTypeValidator.builder()
                        .allowIfSubType(Map.class)
                        .allowIfSubType(List.class)
                        .allowIfSubType(Number.class)
                        .allowIfSubType(CacheConstants.Package.DEFAULT)
                        .allowIfSubTypeIsArray()
                        .build();
        
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        
        objectMapper.findAndRegisterModules();
        
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        objectMapper.activateDefaultTyping(ptv, ObjectMapper.DefaultTyping.NON_FINAL);

		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

		objectMapper.registerModules(
				new JavaTimeModule(),
				new Jdk8Module(),
				new ConstraintViolationProblemModule(),
				new ProblemModule(),
				new AfterburnerModule());

        return objectMapper;
    }


	@Override
	@NonNull
	public CacheErrorHandler errorHandler() {
		return new RedisCacheErrorHandler();
	}
}
