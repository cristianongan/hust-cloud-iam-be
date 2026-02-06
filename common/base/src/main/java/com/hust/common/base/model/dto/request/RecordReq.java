package com.hust.common.base.model.dto.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

@Data
public class RecordReq implements Serializable {

    @Serial
    private static final long serialVersionUID = -1562263900109834653L;

    private String leakId;

    private String dataSource;

    private LocalDate detectTimeStart;

    private LocalDate detectTimeEnd;

    private Integer page;

    private Integer pageSize;
}
