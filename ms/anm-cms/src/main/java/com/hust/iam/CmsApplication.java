package com.hust.iam;

import lombok.extern.slf4j.Slf4j;
import com.hust.common.base.BaseApplication;
import com.hust.common.base.constants.PackageConstants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.env.Environment;

@Slf4j
@SpringBootApplication(scanBasePackages = {PackageConstants.DEFAULT_PACKAGE_SCAN},
exclude = { UserDetailsServiceAutoConfiguration .class })
@EnableConfigurationProperties
public class CmsApplication extends BaseApplication {
    public CmsApplication(Environment env) {
        super(env);
    }

    public static void main(String[] args) {
        _log.info("CmsApplication start");
        SpringApplication app = new SpringApplication(CmsApplication.class);

        init(app, args);
    }
}
