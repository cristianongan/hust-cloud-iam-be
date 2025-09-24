package org.mbg.common.base.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.mbg.common.util.Validator;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public enum CustomerDataType {
    EMAIL(1),
    PHONE(2)
    ;
    private final int value;

    public static List<Integer> getValues() {
        return Stream.of(values()).map(e -> e.value).collect(Collectors.toList());
    }

    public static CustomerDataType valueOfStatus(int value) {
        for (CustomerDataType e : values()) {
            if (e.getValue() == value) {
                return e;
            }
        }

        return null;
    }

    public static CustomerDataType resolveByName(String name) {
        if (name == null) return null;
        try {
            return CustomerDataType.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }
}
