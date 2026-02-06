package com.hust.iam.model.dto;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.mbg.common.base.model.AbstractAuditingEntity;

import java.io.Serial;
import java.io.Serializable;

@Data
public class PermissionDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 952169706560871464L;

    private Long id;

    private String name;

    private String description;

    private String code;
}
