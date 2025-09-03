package org.mbg.anm;

import org.mbg.common.base.BaseApplication;
import org.mbg.common.base.constants.PackageConstants;
import org.mbg.common.security.configuration.AuthenticationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.env.Environment;

@SpringBootApplication(scanBasePackages = {
        PackageConstants.DEFAULT_PACKAGE_ANM,
        PackageConstants.PACKAGE_CACHE,
        PackageConstants.PACKAGE_LABEL
})
@EnableConfigurationProperties({AuthenticationProperties.class})
public class GatewayServiceApplication extends BaseApplication {
    public GatewayServiceApplication(Environment env) {
        super(env);
    }

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(GatewayServiceApplication.class);

        init(app, args);
    }
}
