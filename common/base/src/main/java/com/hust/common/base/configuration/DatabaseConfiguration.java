package com.hust.common.base.configuration;

import com.hust.common.base.constants.BeanConstants;
import com.hust.common.base.constants.PackageConstants;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Configuration
@EnableJpaRepositories({PackageConstants.DEFAULT_JPA_REPOSITORY, PackageConstants.PACKAGE_BASE_REPOSITORY})
@EnableJpaAuditing(auditorAwareRef = BeanConstants.SPRING_SECURITY_AUDITOR_AWARE)
@EnableTransactionManagement
@EntityScan(basePackages = {
        PackageConstants.PACKAGE_BASE,
        PackageConstants.DEFAULT_PACKAGE_SCAN
})
public class DatabaseConfiguration {
	
}
