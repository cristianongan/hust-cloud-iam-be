package org.mbg.common.base.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

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

    @Column(name = "request_id")
    private String requestId;

    @Column(name = "leak_id")
    private String leakId;

    @Column(name = "severity")
    private int severity;

    @Column(name = "description")
    private String description;

    @Column(name = "source")
    private String source;

    @Column(name = "leak_name")
    private String leakName;

    @Column(name = "data_published_time")
    private Long dataPublishedTime;

    @Column(name = "detect_time")
    private Long detectTime; // utc

    @Column(name = "status")
    private int status;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> meta;
}
