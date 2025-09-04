package org.mbg.common.security.jwt;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class JwtAccessToken implements Serializable {
    private JwtToken accessToken;

    private JwtToken refreshToken;
}
