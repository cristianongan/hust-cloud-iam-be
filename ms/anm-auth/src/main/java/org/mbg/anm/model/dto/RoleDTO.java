package org.mbg.anm.model.dto;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.mbg.common.base.model.AbstractAuditingEntity;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class RoleDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 4468540765382411022L;

    private Long id;

    private String name;

    private String description;

    private String code;

    private Integer status;

    private List<String> permissions;
}
