package org.mbg.common.base.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;

/**
 * 07/04/2025 - LinhLH: Create new
 *
 * @author LinhLH
 */
public abstract class BaseEntity {
    @Column(name = "IS_DELETED")
    @JsonIgnore
    private boolean isDeleted;
}
