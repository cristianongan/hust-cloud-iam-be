package org.mbg.anm.model.dto.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class CommonDataReq implements Serializable {
    @Serial
    private static final long serialVersionUID = 2517103948585640608L;

    private String type;

    private String keyword;

    private int page;

    private int pageSize;
}
