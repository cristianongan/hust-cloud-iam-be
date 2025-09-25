package org.mbg.common.base.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.mbg.common.label.LabelKey;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum CustomerSyncStatus {

    NEW(0),

    WAITING(1),

    UPDATED(2),

    CLOSED(3)
    ;


    private int status;

    /**
     * Dùng làm input cho annotation ValueOfEnum
     *
     * @return List<Integer>
     */
    public static List<Integer> getValues() {
        return Stream.of(values()).map(e -> e.status).collect(Collectors.toList());
    }

    public static CustomerSyncStatus valueOfStatus(int status) {
        for (CustomerSyncStatus e : values()) {
            if (e.getStatus() == status) {
                return e;
            }
        }

        return null;
    }
}
