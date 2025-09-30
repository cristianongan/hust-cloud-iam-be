package org.mbg.common.base.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "record_type")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class RecordType  extends AbstractAuditingEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = -7864277728589427988L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type")
    private String type;

    @Column(name = "record_id")
    private Long recordId;
}
