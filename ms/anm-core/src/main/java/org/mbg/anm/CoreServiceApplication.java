package org.mbg.anm;

import org.mbg.common.base.BaseApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.core.env.Environment;

public class CoreServiceApplication extends BaseApplication {
    public CoreServiceApplication(Environment env) {
        super(env);
    }

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(CoreServiceApplication.class);

        init(app, args);
    }
}
