package org.mbg.anm.model.dto.response;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class InfoRes implements Serializable {
    @Serial
    private static final long serialVersionUID = 2856540223712351257L;

    private String customerKey;

    private String clientId;

    private List<CustomerDataRes> data;

    @Data
    @Builder
    public static class CustomerDataRes implements Serializable {
        @Serial
        private static final long serialVersionUID = 6995563939317457513L;

        private String type;

        private String value;

        private Integer verified;

        private Integer isPrimary;
    }
}
