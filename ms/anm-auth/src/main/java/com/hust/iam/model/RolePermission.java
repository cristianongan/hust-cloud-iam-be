package com.hust.iam.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.mbg.common.base.model.AbstractAuditingEntity;

import java.io.Serial;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "role_permission")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class RolePermission extends AbstractAuditingEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 3460327681465140656L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role_code")
    private String roleCode;

    @Column(name = "permission_code")
    private String permissionCode;
}
