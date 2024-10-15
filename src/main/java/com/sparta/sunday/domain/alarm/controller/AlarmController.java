package com.sparta.sunday.domain.alarm.controller;

import com.slack.api.methods.SlackApiException;
import com.sparta.sunday.domain.alarm.dto.AlarmRequest;
import com.sparta.sunday.domain.alarm.dto.AlarmResponse;
import com.sparta.sunday.domain.alarm.service.AlarmService;
import com.sparta.sunday.domain.common.dto.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/users/alarms")
@RequiredArgsConstructor
public class AlarmController {

    private final AlarmService alarmService;

    @PostMapping
    public ResponseEntity<AlarmResponse> saveAlarm(
            @RequestBody AlarmRequest alarmRequest,
            @AuthenticationPrincipal AuthUser authuser) throws IOException, SlackApiException {
        return ResponseEntity.ok(alarmService.saveAlarm(alarmRequest.getType(), alarmRequest.getItemId(), authuser));
    }

    @GetMapping
    public ResponseEntity<List<AlarmResponse>> getAlarms() {
        return ResponseEntity.ok(alarmService.getAlarms());
    }
}
