package org.mbg.anm.model;

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
@Table(name = "client_")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Client extends AbstractAuditingEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -2265100410137595456L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client_id")
    private String clientId;

    @Column(name = "client_name")
    private String clientName;

    @Column(name = "client_secret")
    private String clientSecret;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    private int status;
}
