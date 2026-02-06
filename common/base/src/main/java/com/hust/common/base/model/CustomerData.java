package com.hust.common.base.model;

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
@Table(name = "customer_data")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CustomerData extends AbstractAuditingEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 5085094440067521613L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Column(name = "customer_id")
//    private Long customerId;

    @Column(name = "customer_key")
    private String customerKey;

    @Column(name = "type")
    private Integer type;

    @Column(name = "value")
    private String value;

    @Column(name = "verify")
    private Integer verify;

    @Column(name = "status")
    private Integer status;

    @Column(name = "is_primary")
    private Integer isPrimary;

    @Column(name = "last_scan")
    private LocalDateTime lastScan;

    @Column(name = "sync_status")
    private Integer syncStatus;
}
