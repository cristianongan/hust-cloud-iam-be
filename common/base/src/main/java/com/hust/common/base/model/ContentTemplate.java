package com.hust.common.base.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serial;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "content_template")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ContentTemplate extends AbstractAuditingEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = -9072362243959910495L;

    @Id
    @Column(name = "template_code", unique = true, nullable = false)
    private String templateCode;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "title", length = 255)
    private String title;

    @Column(name = "content", length = 1000)
    private String content;

    @Column(name = "description" , length = 255)
    private String description;

    @Column(name = "status", nullable = false, length = 1)
    private int status;
}
