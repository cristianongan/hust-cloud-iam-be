package org.mbg.anm.model.dto.response;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.mbg.common.base.model.converter.IntegerListConverter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class RecordResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = -9124369472903526312L;

    private Long id;

//    private String requestId;

    private String dataSource;

    private Integer severity;

    private String description;

    private String source;

    private String leakName;

    private Long dataPublishedTime;

    private Long detectTime;

    private Map<String, Object> meta;

    private List<String> types;
}
