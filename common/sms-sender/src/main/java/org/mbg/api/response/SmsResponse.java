package org.mbg.api.response;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class SmsResponse implements Serializable {

    /** The Constant serialVersionUID */
    private static final long serialVersionUID = -598031529712134039L;

    @XmlElement(name = "message")
    private String message;

    /* result=1 -> send thanh cong, result=0 -> send that bai */
    @XmlElement(name = "result")
    private int result;

    public SmsResponse(int result, String mess) {
        this.result = result;
        this.message = mess;
    }

    public SmsResponse(Result result, String mess) {
        this.result = result.getStatus();
        this.message = mess;
    }
    
    @AllArgsConstructor
    public enum Result {
        SUCCESS(1),

        FAILURE(0);

        @Getter
        @Setter
        private int status;
    }
}
