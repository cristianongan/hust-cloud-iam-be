package org.mbg.anm.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.mbg.common.base.model.AbstractAuditingEntity;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Data
public class UserDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -6004336668090775994L;

    private Long id;

    private String username;

    private String email;

    private String phone;

    private int gender;

    private LocalDate dob;

    private String address;

    private int status;

    private String fullname;

    private int type;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    private List<String> permissions;

    private List<String> roles;

}
