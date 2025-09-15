package org.mbg.common.base.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.mbg.common.label.LabelKey;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum RequestStatus {
    DELETED(-1, LabelKey.LABEL_DELETED),

    NEW(0, LabelKey.LABEL_NEW),

    WAITING(1, LabelKey.LABEL_WAITING),

    DONE(2, LabelKey.LABEL_DONE);


    private int status;

    private String key;

    /**
     * Dùng làm input cho annotation ValueOfEnum
     *
     * @return List<Integer>
     */
    public static List<Integer> getValues() {
        return Stream.of(values()).map(e -> e.status).collect(Collectors.toList());
    }

    public static RequestStatus valueOfStatus(int status) {
        for (RequestStatus e : values()) {
            if (e.getStatus() == status) {
                return e;
            }
        }

        return null;
    }
}
