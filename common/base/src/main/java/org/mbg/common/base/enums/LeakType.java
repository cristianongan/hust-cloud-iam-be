package org.mbg.common.base.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum LeakType {
    EMAIL(1),
    PHONE(2),
    BANK_ACCOUNT(3),
    SOCIAL_NETWORK(4),
    ADDRESS(5),
    COMPANY(6),
    TAX(7),
    ONLINE_SHOPPING(8),
    PERSONAL(9),
    INSURANCE(10),
    OTHER(999),
    ;

    final int value;

    public static List<Integer> getValues() {
        return Stream.of(values()).map(e -> e.value).collect(Collectors.toList());
    }

    public static LeakType valueOfStatus(int value) {
        for (LeakType e : values()) {
            if (e.getValue() == value) {
                return e;
            }
        }

        return null;
    }
}
