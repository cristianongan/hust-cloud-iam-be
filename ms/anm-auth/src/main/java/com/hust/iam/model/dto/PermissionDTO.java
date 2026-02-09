package com.hust.iam.model.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class PermissionDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 952169706560871464L;

    private Long id;

    private String name;

    private String description;

    private String code;
}
