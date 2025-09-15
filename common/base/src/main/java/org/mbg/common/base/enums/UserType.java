package org.mbg.common.base.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
@Getter
public enum UserType {
    ADMINISTRATOR(0), // quản trị viên
    BUSINESS(1), // doanh nghiệp
    INDIVIDUAL(2) // cá nhân
    ;
    private int value;

    public static List<Integer> getValues() {
        return Stream.of(values()).map(e -> e.value).collect(Collectors.toList());
    }

    public static UserType valueOfStatus(int value) {
        for (UserType e : values()) {
            if (e.getValue() == value) {
                return e;
            }
        }

        return null;
    }
}
