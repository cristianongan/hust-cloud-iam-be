package com.hust.iam.model.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class ClientDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -8231090254504483806L;

    private Long id;

    private String clientId;

    private String clientName;

    private String description;

    private int status;

    private Long userId;

    private String username;
}
