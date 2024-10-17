package com.sparta.sunday.domain.alarm.service;

import com.slack.api.methods.SlackApiException;
import com.sparta.sunday.domain.alarm.entity.Alarm;
import com.sparta.sunday.domain.alarm.entity.AlarmType;
import com.sparta.sunday.domain.alarm.repository.AlarmRepository;
import com.sparta.sunday.domain.user.entity.User;
import com.sparta.sunday.domain.user.enums.UserRole;
import com.sparta.sunday.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AlarmServiceTest {

    @Mock
    private AlarmRepository alarmRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private SlackAlarmService slackAlarmService;
    @InjectMocks
    private AlarmService alarmService;

    private User testUser;
    private User testUser2;
    private String testMessage;
    private Alarm testAlarm;

    @BeforeEach
    public void setup() {
        testUser = new User(1L, "test@mail.com", UserRole.ROLE_ADMIN);
        testUser2 = new User(1L, "test2@mail.com", UserRole.ROLE_ADMIN);
        testMessage = "test";
        testAlarm = Alarm.of(AlarmType.COMMENT, 1L, testUser, testUser2, testMessage);
    }

    @Test
    @DisplayName("알림 생성 DB에 저장 성공")
    void saveAlarm() throws SlackApiException, IOException {
        // given
        given(userRepository.findByEmail(anyString())).willReturn(Optional.of(testUser));
        // when
        alarmService.saveAlarm(testAlarm.getAlarmType(), testAlarm.getItemId(), testUser, testUser2.getEmail());
        // then
        verify(slackAlarmService).sendSlackAlarm(testUser2.getEmail(), testUser.getUsername() + "님께서 " + testAlarm.getAlarmType().getMessage());
        verify(alarmRepository).save(any(Alarm.class)); // Alarm 객체 저장 여부 검증

    }
}