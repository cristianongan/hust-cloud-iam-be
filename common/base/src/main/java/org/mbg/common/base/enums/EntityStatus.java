package org.mbg.common.base.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.mbg.common.label.LabelKey;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum EntityStatus {
    DELETED(-1, LabelKey.LABEL_DELETED),

    INACTIVE(0, LabelKey.LABEL_INACTIVE),

    ACTIVE(1, LabelKey.LABEL_ACTIVE);


    private Integer status;

    private String key;

    /**
     * Dùng làm input cho annotation ValueOfEnum
     *
     * @return List<Integer>
     */
    public static List<Integer> getValues() {
        return Stream.of(values()).map(e -> e.status).collect(Collectors.toList());
    }

    public static EntityStatus valueOfStatus(int status) {
        for (EntityStatus e : values()) {
            if (e.getStatus() == status) {
                return e;
            }
        }

        return null;
    }
}
