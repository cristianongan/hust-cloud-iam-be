package org.mbg.common.base.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum LeakType {
    EMAIL("email"),
    PHONE("phone"),
    BANK_ACCOUNT("bank_account"),
    ADDRESS("address"),
    IDENTIFICATION("identification"),
    PASSWORD("password"),
    ;

    final String value;

    public static List<String> getValues() {
        return Stream.of(values()).map(e -> e.value).collect(Collectors.toList());
    }

    public static LeakType valueOfType(String value) {
        for (LeakType e : values()) {
            if (Objects.equals(e.getValue(), value)) {
                return e;
            }
        }

        return null;
    }
}
