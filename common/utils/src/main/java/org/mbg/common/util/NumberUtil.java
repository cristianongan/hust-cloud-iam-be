package org.mbg.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NumberUtil {
    public static boolean isConflictBetweenNumber(Long startSource, Long endSource, Long startValue, Long endValue) {
        return (startSource <= startValue && endValue <= endSource)
                || (startValue <= startSource && startSource <= endValue)
                || (startValue <= endSource && endSource <= endValue);
    }
}
