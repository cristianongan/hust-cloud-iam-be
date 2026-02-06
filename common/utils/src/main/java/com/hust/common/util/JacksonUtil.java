package com.hust.common.util;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.zalando.problem.jackson.ProblemModule;
import org.zalando.problem.violations.ConstraintViolationProblemModule;

import java.time.format.DateTimeFormatter;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JacksonUtil {
    public static <T> MultiValueMap<String, String> mapToParams(T request) {
        ObjectMapper objectMapper = new ObjectMapper();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        Map<String, String> fieldMap = objectMapper
                        .convertValue(request, new TypeReference<>() {
                        });

        params.setAll(fieldMap);

        return params;
    }
    
    public static ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper()
                        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.configure(Feature.AUTO_CLOSE_SOURCE, true);

        objectMapper.findAndRegisterModules();

        JavaTimeModule timeModule = new JavaTimeModule();
        
        timeModule.addSerializer(
                        new LocalDateSerializer(DateTimeFormatter.ofPattern(DateUtil.SHORT_DATE_PATTERN_DASH)));
        timeModule.addSerializer(
                        new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DateUtil.LONG_DATE_PATTERN_DASH)));
        
        objectMapper.registerModules(
                        timeModule,
                        new Jdk8Module(),
                        new ConstraintViolationProblemModule(),
                        new ProblemModule(),
                        new AfterburnerModule());

        return objectMapper;
    }
}
