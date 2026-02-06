package com.hust.common.base.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public enum RecordStatus {
    INIT(1),
    UPDATED(2)
    ;

    final int value;

    public static List<Integer> getValues() {
        return Stream.of(values()).map(e -> e.value).collect(Collectors.toList());
    }

    public static RecordStatus valueOfStatus(int value) {
        for (RecordStatus e : values()) {
            if (e.getValue() == value) {
                return e;
            }
        }

        return null;
    }
}
