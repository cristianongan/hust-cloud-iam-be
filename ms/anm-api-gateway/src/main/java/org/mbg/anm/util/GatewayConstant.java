package org.mbg.anm.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Utility class holding constants specific to the Gateway module.
 * This class cannot be instantiated or subclassed.
 *
 * @author: LinhLH
 **/

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class GatewayConstant {

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Metadata {
        public static final String SERVICE_ID = "service-id";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Uri {
        public static final String COMMON_FALLBACK_URI = "forward:/fallback/service-unavailable";
        public static final String LOGOUT_URI = "{baseUrl}/logout";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class PropertyPrefix {
        public static final String KEYCLOAK_CONFIG = "keycloak.client";

        public static final String GATEWAY_CONFIG = "app.gateway";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Keycloak {
        public static final String ACCESS_TOKEN = "access_token";
        public static final String SECRET = "secret";
    }
}
