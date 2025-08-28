package org.mbg.common.base.configuration;

import org.mbg.common.base.constants.BeanConstants;
import org.mbg.common.base.constants.PackageConstants;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Configuration
@EnableJpaRepositories(PackageConstants.DEFAULT_JPA_REPOSITORY)
@EnableJpaAuditing(auditorAwareRef = BeanConstants.SPRING_SECURITY_AUDITOR_AWARE)
@EnableTransactionManagement
public class DatabaseConfiguration {
	
}
