package org.mbg.anm.model.dto.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class RoleReq implements Serializable {
    @Serial
    private static final long serialVersionUID = 4762715366565884794L;

    private Long id;

    private String name;

    private String code;

    private String description;

    private List<String> permissions;

    private List<Long> userIds;

    private List<Long> ids;

    private String keyword;

    private int page;

    private int pageSize;

}
