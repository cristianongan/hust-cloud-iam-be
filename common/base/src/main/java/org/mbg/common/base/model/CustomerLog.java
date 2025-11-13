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
@Table(name = "customer_log")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CustomerLog extends AbstractAuditingEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 3779081063111267165L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_key")
    private String customerKey;

    @Column(name = "subscriber_id")
    private String subscriberId;

    @Column(name = "reference")
    private String reference;

    @Column(name = "status")
    private Integer status;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;
}
