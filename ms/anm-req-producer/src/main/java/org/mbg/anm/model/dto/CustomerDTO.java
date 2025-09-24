package org.mbg.anm.model.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class CustomerDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -7788288436050635033L;

    private Long id;

    private String customerKey;

    private String subscriberId;

    private String clientId;

    private String reference;

    private Integer status;

    private LocalDateTime lastScan;

    private LocalDateTime waitAtLease;
}
