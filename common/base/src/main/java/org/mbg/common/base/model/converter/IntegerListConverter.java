package org.mbg.common.base.model.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.mbg.common.util.StringPool;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Converter(autoApply = false)
public class IntegerListConverter implements AttributeConverter<List<Integer>, String> {
    @Override
    public String convertToDatabaseColumn(List<Integer> integers) {
        if (integers == null || integers.isEmpty()) return StringPool.BLANK;
        return integers.stream()
                .filter(Objects::nonNull)
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }

    @Override
    public List<Integer> convertToEntityAttribute(String s) {
        if (s == null || s.isBlank()) return List.of();
        return Arrays.stream(s.split(","))
                .map(String::trim)
                .filter(i -> !i.isEmpty())
                .map(Integer::valueOf)
                .collect(Collectors.toList());
    }
}
