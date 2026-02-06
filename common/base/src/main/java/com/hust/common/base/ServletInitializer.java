package com.hust.common.base;

import com.hust.common.cache.CacheProperties;
import com.hust.common.cache.CacheRedisConfiguration;
import com.hust.common.cache.CachingHttpHeadersFilter;
import com.hust.common.security.configuration.AuthenticationProperties;
import com.hust.common.util.EnvConstants;
import com.hust.common.util.Validator;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.FilterRegistration;
import jakarta.servlet.ServletContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
//import org.redisson.api.RedissonClient;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.EnumSet;
import java.util.Objects;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ServletInitializer extends SpringBootServletInitializer implements ServletContextInitializer {
	private final AuthenticationProperties ap;

	private final Environment env;

	private final ApplicationContext context;

	@PostConstruct
	public void connection() { 
		RedisTemplate<String, String> redisTemplate = null;

		CacheRedisConfiguration redisConfig = null;

//		RedissonClient redissonClient = null;

		try {
			redisTemplate = (RedisTemplate) this.context.getBean(RedisTemplate.class);

			redisConfig = this.context.getBean(CacheRedisConfiguration.class);
		} catch (Exception e) {
			_log.error("RedisTemplate is not configured");
		}

		try {
//			redissonClient = this.context.getBean(RedissonClient.class);
		} catch (Exception e) {
			_log.error("RedissonClient is not configured");
		}

		try {
			if (Objects.nonNull(redisTemplate) && Objects.nonNull(redisConfig)) {
				if (Validator.equals(Objects.requireNonNull(redisConfig).getMode(),
						CacheProperties.Mode.STANDALONE.name().toLowerCase())) {
				    Objects.requireNonNull(redisTemplate.getConnectionFactory()).getConnection();
				} else {
				    Objects.requireNonNull(redisTemplate.getConnectionFactory()).getSentinelConnection();
				}
			}
//			else if (Validator.isNotNull(redissonClient)) {
//			    Objects.requireNonNull(Objects.requireNonNull(redissonClient).getConfig()).getConnectionListener();
//			}
		} catch (Exception e) {
			System.out.println(
					"-------------------------------------------------------------------------------------------");
			System.out.println("- Redis host and port is not available. please check application configuration file.");
			System.out.println("----------------------------------------------------------------------------------");

			System.exit(-1);
		}
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(BaseApplication.class);
	}

	@Override
	public void onStartup(ServletContext servletContext) {
		if (env.getActiveProfiles().length != 0) {
			_log.info("Web application configuration, using profiles: {}", (Object[]) env.getActiveProfiles());
		}

		EnumSet<DispatcherType> disps =
				EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.ASYNC);

		if (env.acceptsProfiles(Profiles.of(EnvConstants.Profile.PRODUCTION))) {
			initCachingHttpHeadersFilter(servletContext, disps);
		}

		_log.info("Web application fully configured");
	}

	/**
	 * Initializes the caching HTTP Headers Filter.
	 */
	private void initCachingHttpHeadersFilter(ServletContext servletContext, EnumSet<DispatcherType> disps) {
		_log.info("Registering Caching HTTP Headers Filter");

		FilterRegistration.Dynamic cachingHttpHeadersFilter =
				servletContext.addFilter("cachingHttpHeadersFilter", new CachingHttpHeadersFilter());

		cachingHttpHeadersFilter.addMappingForUrlPatterns(disps, true, this.ap.getCache().getUrlPatterns());

		cachingHttpHeadersFilter.setAsyncSupported(true);
	}
}
