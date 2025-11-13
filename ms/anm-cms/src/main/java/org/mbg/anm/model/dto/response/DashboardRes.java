package org.mbg.anm.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class DashboardRes implements Serializable {
    @Serial
    private static final long serialVersionUID = -291653884476748093L;

    private List<Rate> typeRates;

    private List<Count> subscribeCounts;

    private List<Count> recordCounts;

    @Data
    @NoArgsConstructor
    @Builder
    @AllArgsConstructor
    public static class Rate implements Serializable {

        @Serial
        private static final long serialVersionUID = -2729021936479556647L;

        private String value;

        private Double rate;
    }

    @Data
    @NoArgsConstructor
    @Builder
    @AllArgsConstructor
    public static class Count implements Serializable {

        @Serial
        private static final long serialVersionUID = 3833898039567497031L;

        private LocalDate time;

        private Long count;
    }
}
