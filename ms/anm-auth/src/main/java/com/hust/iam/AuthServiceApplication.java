package com.hust.iam;

import com.hust.common.base.BaseApplication;
import com.hust.common.base.constants.PackageConstants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.core.env.Environment;

@SpringBootApplication(scanBasePackages = {PackageConstants.DEFAULT_PACKAGE_SCAN})
@EnableConfigurationProperties
@EnableFeignClients
public class AuthServiceApplication extends BaseApplication {
    public AuthServiceApplication(Environment env) {
        super(env);
    }

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(AuthServiceApplication.class);

        init(app, args);
    }
}
