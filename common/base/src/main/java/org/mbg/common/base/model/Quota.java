package org.mbg.common.base.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "quota")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Quota extends AbstractAuditingEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -4287076703880899717L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "in_use")
    private Long inUse;

    @Column(name = "total")
    private Long total;

    @Column(name = "user_id")
    private Long userId;
}
