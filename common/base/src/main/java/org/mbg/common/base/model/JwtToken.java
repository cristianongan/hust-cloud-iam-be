package org.mbg.common.base.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtToken {
    private String token;

    private int duration;
}
