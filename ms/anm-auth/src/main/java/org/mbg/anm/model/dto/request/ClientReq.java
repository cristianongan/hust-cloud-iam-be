package org.mbg.anm.model.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class ClientReq {
    private String username;

    private Long userId;

    private String clientId;

    private List<String> ids;

    private int page;

    private int pageSize;
}
