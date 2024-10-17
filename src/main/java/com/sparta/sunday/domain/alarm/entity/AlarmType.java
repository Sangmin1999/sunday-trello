package com.sparta.sunday.domain.alarm.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum AlarmType {
    MEMBER("맴버를 추가하였습니다."),
    CARD("카드를 추가하였습니다."),
    COMMENT("댓글을 추가하였습니다.");

    private final String message;

    public static AlarmType of(String type) {
        return Arrays.stream(AlarmType.values())
                .filter(t -> t.name().equalsIgnoreCase(type))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 type"));
    }
}
