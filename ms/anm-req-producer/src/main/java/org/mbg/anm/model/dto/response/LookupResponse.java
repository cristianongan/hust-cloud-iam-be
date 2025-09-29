package org.mbg.anm.model.dto.response;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class LookupResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = -5401288388738213439L;

    private String subscriberId;

    private Page<RecordResponse> page;
}
