package org.mbg.anm.consumer.response;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class LeakCheckResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 843285259738980049L;

    private boolean success;

    private Integer found;

    private List<String> fields;

    private List<Source> sources;


    @Data
    public static class Source implements Serializable {

        @Serial
        private static final long serialVersionUID = -4261325545944717386L;

        private String name;

        private String date;
    }
}
