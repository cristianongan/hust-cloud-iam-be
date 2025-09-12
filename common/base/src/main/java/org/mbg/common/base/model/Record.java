package org.mbg.common.base.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "record_")
@Data
@EqualsAndHashCode(callSuper = false)
public class Record extends AbstractAuditingEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = -1224575857778503851L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String requestId;

    private String data;

    private String filter;

    private int severity;

    private String description;

    private String source;

    private String leakName;

    private Long dataPublishedTime;

    private Long detectTime; // utc
}
