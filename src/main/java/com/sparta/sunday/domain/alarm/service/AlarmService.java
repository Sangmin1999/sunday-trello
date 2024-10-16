package com.sparta.sunday.domain.alarm.service;

import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.request.users.UsersLookupByEmailRequest;
import com.slack.api.methods.response.users.UsersLookupByEmailResponse;
import com.sparta.sunday.domain.alarm.entity.Alarm;
import com.sparta.sunday.domain.alarm.entity.AlarmType;
import com.sparta.sunday.domain.alarm.repository.AlarmRepository;
import com.sparta.sunday.domain.user.entity.User;
import com.sparta.sunday.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AlarmService {

    @Value(value = "${slack.bot-token}")
    private String slackToken;

    private final AlarmRepository alarmRepository;
    private final UserRepository userRepository;


    @Transactional
    public void saveAlarm(AlarmType alarmType, Long itemId, User sendUser, String receiveUserEmail) throws IOException, SlackApiException {

        User receiveUser = userRepository.findByEmail(receiveUserEmail)
                .orElseThrow(() -> new IllegalArgumentException("없는 유저"));

        String sb = sendUser.getUsername() + "님께서 " + alarmType.getMessage();

        MethodsClient client = Slack.getInstance().methods();

        UsersLookupByEmailResponse response1 = client.usersLookupByEmail(UsersLookupByEmailRequest.builder()
                .token(slackToken)
                .email(receiveUserEmail)
                .build());

        Alarm alarm = Alarm.of(alarmType, itemId, receiveUser, sendUser, sb);

        alarmRepository.save(alarm);

        ChatPostMessageRequest request = ChatPostMessageRequest.builder()
                .token(slackToken)
                .channel(response1.getUser().getId())
                .text(sb)
                .build();

        client.chatPostMessage(request);
    }

}
