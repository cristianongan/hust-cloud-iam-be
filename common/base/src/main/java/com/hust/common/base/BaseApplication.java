package com.hust.common.base;

import com.hust.common.base.configuration.DefaultProfileUtil;
import com.hust.common.util.EnvConstants;
import com.hust.common.util.StringPool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

/**
 * 16/08/2022 - LinhLH: Create new
 *
 * @author LinhLH
 */
@Slf4j
@Component
@EnableAspectJAutoProxy
public abstract class BaseApplication implements InitializingBean {
    private static Environment env;

    public BaseApplication(Environment env) {
        BaseApplication.env = env;
    }
    
    public static void init(SpringApplication app, String[] args) {
        DefaultProfileUtil.addDefaultProfile(app);

        app.run(args);
        
        logApplicationStartup();
    }

    private static void logApplicationStartup() {
        String protocol = EnvConstants.Protocol.HTTP;

        if (env.getProperty(EnvConstants.Properties.SERVER_SSL_KEY_STORE) != null) {
            protocol = EnvConstants.Protocol.HTTPS;
        }

        String serverPort = env.getProperty(EnvConstants.Properties.SERVER_PORT);
        String contextPath = env.getProperty(EnvConstants.Properties.SERVER_SERVLET_CONTEXT_PATH);

        if (Objects.isNull(contextPath)) {
            contextPath = StringPool.SLASH;
        }

        String hostAddress = StringPool.LOCALHOST;

        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            _log.warn("The host name could not be determined, using `localhost` as fallback");
        }

        _log.info(
                """
                        
                        ----------------------------------------------------------
                        Application '{}' is running! Access URLs:
                        Local: {}://localhost:{}{}
                        External: {}://{}:{}{}
                        Profile(s): {}
                        ----------------------------------------------------------""",
                        env.getProperty(EnvConstants.Properties.SPRING_APPLICATION_NAME), protocol, serverPort,
                        contextPath,
                        protocol, hostAddress,
                        serverPort, contextPath, env.getActiveProfiles());

        _log.info("Server has been started");
    }

    @Override
    public void afterPropertiesSet() {
        Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
        
        if (activeProfiles.contains(EnvConstants.Profile.DEVELOPMENT)
                        && activeProfiles.contains(EnvConstants.Profile.PRODUCTION)) {
            _log.error("You have misconfigured your application! It should not run "
                            + "with both the 'dev' and 'prod' profiles at the same time.");
        }
    }
}
