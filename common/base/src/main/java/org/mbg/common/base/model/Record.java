package org.mbg.common.base.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.mbg.common.base.enums.LeakType;
import org.mbg.common.base.enums.RecordStatus;
import org.mbg.common.base.model.converter.IntegerListConverter;
import org.mbg.common.base.util.PiiScanner;
import org.mbg.common.util.Validator;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> meta;

    @Convert(converter = IntegerListConverter.class)
    @Column(name = "types", columnDefinition = "varchar(255)")
    private List<Integer> types;

    public Record(String requestId, String leakId, String dataSource,
                  String leakName, Long dataPublishedTime, Long detectTime, Integer severity,
                  Map<String, Object> meta, String description) {
        this.requestId = requestId;
        this.dataSource = dataSource;
        this.leakId = leakId;
        this.leakName = leakName;
        this.dataPublishedTime = dataPublishedTime;
        this.detectTime = detectTime;
        this.meta = meta;
        this.severity = severity;
        this.status = RecordStatus.INIT.getValue();
        this.types = this.getTypes();
        this.description = description;
    }

    private List<Integer> getTypes() {
        if (Validator.isNotNull(meta)) {
            return PiiScanner.convertToEntityAttribute(meta.keySet());
        }

        return List.of();
    }
}
