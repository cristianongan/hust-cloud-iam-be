package com.hust.iam.model.dto;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@Data
public class RecordDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 4090041733243470625L;

    private Long id;

    private String requestId;

    private String subscriberId;

    private String dataSource;

    private String leakId;

    private Integer severity;

    private String description;

    private String source;

    private String leakName;

    private Long dataPublishedTime;

    private Long detectTime;

    private Integer status;

    private Map<String, Object> meta;

    private List<String> types;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;
}
