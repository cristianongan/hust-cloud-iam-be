package org.mbg.anm.configuration;

import org.mbg.anm.util.GatewayConstant;
import lombok.Getter;
import lombok.Setter;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.authorization.client.Configuration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Represents the configuration for a Keycloak Authorization Client.
 * This class is responsible for loading configuration properties
 * prefixed with "keycloak.client" as defined in the application
 * properties or YAML file.
 * <p>
 * This class also provides a bean for configuring and creating
 * an instance of the Keycloak AuthzClient, which is used for
 * interacting with Keycloak's authorization server.
 * <p>
 * The fields of this class map to Keycloak's client configuration
 * parameters such as realm, authorization server URL, client ID,
 * client secret, and other security-related configurations.
 * <p>
 * The AuthzClient bean created by this class initializes itself
 * using the provided configuration values. It is used by other
 * components to perform authentication and authorization
 * operations against the Keycloak server.
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = GatewayConstant.PropertyPrefix.KEYCLOAK_CONFIG)
public class AuthzClientConfiguration {
    private String authServerUrl;

    private String realm;

    private String resource;

    private boolean publicClient;

    private Map<String, Object> credentials;

    private String sslRequired;

    /**
     * Configures and provides a bean for the Keycloak AuthzClient.
     * This method creates an instance of AuthzClient using the
     * provided configuration properties including realm, authorization
     * server URL, SSL requirements, client ID, client secret, and
     * whether the client is public or confidential.
     * <p>
     * The generated AuthzClient is used to interact with Keycloak's
     * authorization server, enabling authentication and authorization
     * operations for the application.
     * <p>
     * If the client is confidential, the method sets the client secret
     * into the credentials within the configuration.
     *
     * @return an instance of AuthzClient initialized with the specified configuration
     */
    @Bean
    public AuthzClient authzClient() {
        Configuration configuration = new Configuration(
                this.authServerUrl,
                this.realm,
                this.resource,
                this.credentials,
                null
        );

        configuration.setPublicClient(this.publicClient);

        return AuthzClient.create(configuration);
    }
}