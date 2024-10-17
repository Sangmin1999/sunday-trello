package com.sparta.sunday.domain.alarm.service;

import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.request.users.UsersLookupByEmailRequest;
import com.slack.api.methods.response.users.UsersLookupByEmailResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SlackAlarmService {

    @Value(value = "${slack.bot-token}")
    private String slackToken;

    @Async
    public void sendSlackAlarm(String receiveUserEmail, String message) throws SlackApiException, IOException {

        MethodsClient client = Slack.getInstance().methods();

        UsersLookupByEmailResponse response = client.usersLookupByEmail(UsersLookupByEmailRequest.builder()
                .token(slackToken)
                .email(receiveUserEmail)
                .build());

        ChatPostMessageRequest request = ChatPostMessageRequest.builder()
                .token(slackToken)
                .channel(response.getUser().getId())
                .text(message)
                .build();

        client.chatPostMessage(request);

    }

}
