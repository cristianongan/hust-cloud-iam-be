package com.hust.common.base.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public enum PermissionType {
    ADMINISTRATOR(0),
    USER(1),
    CLIENT(2),
    ;

    private final int value;

    public static List<Integer> getValues() {
        return Stream.of(values()).map(e -> e.value).collect(Collectors.toList());
    }

    public static PermissionType valueOf(int input) {
        for (PermissionType e : values()) {
            if (e.getValue() == input) {
                return e;
            }
        }

        return null;
    }
}
