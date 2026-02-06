package com.hust.common.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Md5Util {
    private static final ObjectMapper MAPPER = new ObjectMapper();


    private static final ObjectMapper CANON = JsonMapper.builder()
            .configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true)
            .serializationInclusion(JsonInclude.Include.NON_NULL)
            .build()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    public static String md5Hex(Object obj) {
        try {
            String json = CANON.writeValueAsString(obj);
            byte[] md5 = MessageDigest.getInstance("MD5")
                    .digest(json.getBytes(StandardCharsets.UTF_8));
            return toHex(md5);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String toHex(byte[] b) {
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (byte x : b) sb.append(String.format("%02x", x));
        return sb.toString();
    }

    public static String md5OfJson(String json) {
        try {
            JsonNode node = MAPPER.readTree(json);
            String canonical = canonicalize(node);
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(canonical.getBytes(StandardCharsets.UTF_8));
            return java.util.HexFormat.of().formatHex(digest);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String canonicalize(JsonNode node) {
        if (node == null || node.isNull()) return "null";
        if (node.isObject()) {
            ObjectNode obj = (ObjectNode) node;
            List<String> names = new ArrayList<>();
            obj.fieldNames().forEachRemaining(names::add);
            Collections.sort(names);

            return names.stream()
                    .map(n -> quote(n) + ":" + canonicalize(obj.get(n)))
                    .collect(Collectors.joining(",", "{", "}"));
        }
        if (node.isArray()) {
            ArrayNode arr = (ArrayNode) node;
            List<String> items = new ArrayList<>();
            for (JsonNode item : arr) items.add(canonicalize(item));
            return items.stream().collect(Collectors.joining(",", "[", "]"));
        }
        if (node.isTextual()) return quote(node.textValue());
        if (node.isNumber()) return node.numberValue().toString();
        if (node.isBoolean()) return node.booleanValue() ? "true" : "false";
        return node.toString();
    }

    private static String quote(String s) {
        try {
            return MAPPER.writeValueAsString(s);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
