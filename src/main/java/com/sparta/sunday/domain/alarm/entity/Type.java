package com.sparta.sunday.domain.alarm.entity;

import java.util.Arrays;

public enum Type {
    MEMBER, CARD, COMMENT;

    public static Type of(String type) {
        return Arrays.stream(Type.values())
                .filter(t -> t.name().equalsIgnoreCase(type))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 type"));
    }
}
