package com.hust.iam.model.dto.response;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class RecordTypeStatisticRes implements Serializable {
    @Serial
    private static final long serialVersionUID = -4703206658506334281L;

    List<ItemRes> data;

    @Data
    @Builder
    public static class ItemRes implements Serializable {

        @Serial
        private static final long serialVersionUID = -2170685616348379615L;

        private String type;

        private Long count;
    }
}
