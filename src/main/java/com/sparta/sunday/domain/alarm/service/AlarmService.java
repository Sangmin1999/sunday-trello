package com.sparta.sunday.domain.alarm.service;

import com.slack.api.methods.SlackApiException;
import com.sparta.sunday.domain.alarm.entity.Alarm;
import com.sparta.sunday.domain.alarm.entity.AlarmType;
import com.sparta.sunday.domain.alarm.repository.AlarmRepository;
import com.sparta.sunday.domain.user.entity.User;
import com.sparta.sunday.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AlarmService {

    private final AlarmRepository alarmRepository;
    private final UserRepository userRepository;
    private final SlackAlarmService slackAlarmService;


    @Transactional
    public void saveAlarm(AlarmType alarmType, Long itemId, User sendUser, String receiveUserEmail) throws IOException, SlackApiException {

        if (sendUser.getEmail() != receiveUserEmail) {

            User receiveUser = userRepository.findByEmail(receiveUserEmail)
                    .orElseThrow(() -> new IllegalArgumentException("없는 유저"));

            String message = sendUser.getUsername() + "님께서 " + alarmType.getMessage();

            slackAlarmService.sendSlackAlarm(receiveUserEmail, message);

            Alarm alarm = Alarm.of(alarmType, itemId, receiveUser, sendUser, message);

            alarmRepository.save(alarm);

        }

    }

}
