package org.mbg.anm.jwt;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
public class JwtAccessToken implements Serializable {
    @Serial
    private static final long serialVersionUID = -3506332685642222669L;

    private JwtToken accessToken;

    private JwtToken refreshToken;
}
