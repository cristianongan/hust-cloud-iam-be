package com.hust.common.base.configuration;

import liquibase.integration.spring.SpringLiquibase;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Getter
@Setter
@Configuration
//@ConfigurationProperties(prefix = "spring.liquibase")
@EnableCaching
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "spring.liquibase", name = "enabled", havingValue = "true")
@ConditionalOnBean(DataSource.class)
public class LiquibaseConfiguration {
	private String changeLog;

	private final DataSource dataSource;

	@Bean
	public SpringLiquibase liquibase() {
		SpringLiquibase liquibase = new SpringLiquibase();
		
		liquibase.setDataSource(this.dataSource);
		
		liquibase.setChangeLog(this.changeLog);
		
		return liquibase;
	}
}
