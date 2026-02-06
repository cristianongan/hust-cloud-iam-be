package com.hust.common.base.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

/**
 * Base abstract class for entities which will hold definitions for created, last modified by and created,
 * last modified by date.
 */
@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractAuditingEntity implements Serializable {

	@Serial
	private static final long serialVersionUID = 2672912996730315557L;

	@CreatedBy
	@Column(name = "CREATED_BY", updatable = false, length = 75)
	@JsonIgnore
	private String createdBy;

	@CreatedDate
	@Column(name = "CREATED_DATE", updatable = false)
	@JsonIgnore
	private Instant createdDate = Instant.now();

	@LastModifiedBy
	@Column(name = "LAST_MODIFIED_BY", length = 75)
	@JsonIgnore
	private String lastModifiedBy;

	@LastModifiedDate
	@Column(name = "LAST_MODIFIED_DATE")
	@JsonIgnore
	private Instant lastModifiedDate = Instant.now();
}
