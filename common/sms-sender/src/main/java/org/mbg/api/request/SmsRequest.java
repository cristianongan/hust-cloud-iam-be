package org.mbg.api.request;

import java.io.Serial;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.mbg.common.api.request.Request;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@SuperBuilder
public class SmsRequest extends Request implements Serializable {
    @Serial
    private static final long serialVersionUID = 1796694357301604065L;

    private String from;
    
    private String to;
    
    private String title;

    private String content;

}
