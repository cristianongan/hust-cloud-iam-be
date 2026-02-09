package com.hust.common.base.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import com.hust.common.base.enums.RecordStatus;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "record_")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Record extends AbstractAuditingEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = -1224575857778503851L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "request_id")
    private String requestId;

//    @Column(name = "customer_id")
//    private Long customerId;

    @Column(name = "customer_key")
    private String customerKey;

    @Column(name = "data_source")
    private String dataSource;

    @Column(name = "leak_id")
    private String leakId;

    @Column(name = "severity")
    private Integer severity;

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
    private Integer status;

    @Column(name = "data_lookup")
    private String dataLookup;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> meta;

    @ElementCollection
    @CollectionTable(
            name = "record_type",
            joinColumns = @JoinColumn(name = "record_id")
    )
    @Column(name = "type")
    private List<String> types;

    public Record(String customerKey, String requestId, String leakId, String dataSource,
                  String leakName, Long dataPublishedTime, Long detectTime, Integer severity,
                  Map<String, Object> meta, String description, List<String> types) {
        this.customerKey = customerKey;
        this.requestId = requestId;
        this.dataSource = dataSource;
        this.leakId = leakId;
        this.leakName = leakName;
        this.dataPublishedTime = dataPublishedTime;
        this.detectTime = detectTime;
        this.meta = meta;
        this.severity = severity;
        this.status = RecordStatus.INIT.getValue();
        this.description = description;
        this.types = types;
    }
}
