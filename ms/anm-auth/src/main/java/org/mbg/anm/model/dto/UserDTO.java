package org.mbg.anm.model.dto;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.mbg.common.base.model.AbstractAuditingEntity;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

@Data
public class UserDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -6004336668090775994L;

    private Long id;

    private String username;

    private String password;

    private String email;

    private String phone;

    private int gender;

    private LocalDate dob;

    private String address;

    private int status;

    private String fullname;

}
