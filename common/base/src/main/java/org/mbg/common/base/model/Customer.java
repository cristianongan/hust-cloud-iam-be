package org.mbg.common.base.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "customer")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Customer extends AbstractAuditingEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 3779081063181267165L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_key")
    private String customerKey;

    @Column(name = "subscriber_id")
    private String subscriberId;

    @Column(name = "client_id")
    private String clientId;

    @Column(name = "reference")
    private String reference;

    @Column(name = "status")
    private Integer status;

    @Column(name = "last_scan")
    private LocalDateTime lastScan;

    @Column(name = "wait_at_lease")
    private LocalDateTime waitAtLease;
}
