package org.mbg.anm.consumer.response;

import com.google.gson.JsonObject;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mbg.common.api.response.Response;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
public class GroupIbCompromisedRes extends GroupIbResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 1364831864860238776L;

    private String resultId;
    private Integer count;
    private List<Item> items;
    private Long seqUpdate;

    @Data
    public static class Item {
        private JsonObject addInfo;
        private String description;
        private List<String> downloadLinkList;
        private List<String> email;
        private Evaluation evaluation;
        private String id;
        private String leakName;
        private String leakPublished;   // ví dụ "04.09.2025"
        private List<String> password;  // "Hidden data" ở mẫu
        private String reaperMessageId;
        private List<String> taName;
        private String updateTime;      // ví dụ "2025-09-09T18:04:21"
        private String uploadTime;
    }

    @Data
    public static class Evaluation {
        private String  admiraltyCode; // "C3"
        private Integer credibility;   // 50
        private Integer reliability;   // 50
        private String  severity;      // "green"
        private String  tlp;           // "amber"
        private String  ttl;
    }
}
