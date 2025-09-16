package org.mbg.anm;

import org.mbg.common.base.BaseApplication;
import org.mbg.common.base.constants.PackageConstants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {PackageConstants.DEFAULT_PACKAGE_SCAN},
        exclude = { UserDetailsServiceAutoConfiguration.class }
)
@EnableConfigurationProperties
public class ReqProducerApplication extends BaseApplication {

    public ReqProducerApplication(Environment env) {
        super(env);
    }

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ReqProducerApplication.class);

        init(app, args);
    }
}
