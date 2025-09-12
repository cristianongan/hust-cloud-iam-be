package org.mbg.common.base.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum LeakSeverity {
    LOW(1),
    MEDIUM(2),
    HIGH(3),
    CRITICAL(4)
    ;

    private final int value;

    public static List<Integer> getValues() {
        return Stream.of(values()).map(e -> e.value).collect(Collectors.toList());
    }

    public static LeakSeverity valueOfStatus(int value) {
        for (LeakSeverity e : values()) {
            if (e.getValue() == value) {
                return e;
            }
        }

        return null;
    }
}
