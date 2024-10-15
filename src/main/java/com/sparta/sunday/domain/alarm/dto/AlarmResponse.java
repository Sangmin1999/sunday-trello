package com.sparta.sunday.domain.alarm.dto;

import com.sparta.sunday.domain.alarm.entity.Type;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class AlarmResponse {

    private final Long alarmId;
    private final Long receiveUserId;
    private final Long sendUserId;
    private final Type type;
    private final Long itemId;
    private final String message;
    private final LocalDateTime createdAt;

}
