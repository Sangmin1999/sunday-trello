package com.sparta.sunday.domain.alarm.service;

import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.sparta.sunday.domain.alarm.dto.AlarmResponse;
import com.sparta.sunday.domain.alarm.entity.Alarm;
import com.sparta.sunday.domain.alarm.repository.AlarmRepository;
import com.sparta.sunday.domain.common.dto.AuthUser;
import com.sparta.sunday.domain.user.entity.User;
import com.sparta.sunday.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlarmService {

    @Value(value = "${slack.bot-token}")
    private String slackToken;

    @Value(value = "${slack.channel.monitor}")
    private String channel;

    private final AlarmRepository alarmRepository;
    private final UserRepository userRepository;


    @Transactional
    public AlarmResponse saveAlarm(String type, Long itemId, AuthUser authUser) throws IOException, SlackApiException {

        User sendUser = userRepository.findById(authUser.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("없는 유저"));

        String sb = sendUser.getUsername() + "님께서 " + typeMessage(type);

        Alarm alarm = Alarm.of(type, itemId, /*receiveUser 로 변경 필요*/ sendUser, sendUser, sb);

        alarmRepository.save(alarm);

        MethodsClient client = Slack.getInstance().methods();

        ChatPostMessageRequest request = ChatPostMessageRequest.builder()
                .token(slackToken)
                .channel(channel)
                .text(sb)
                .build();

        client.chatPostMessage(request);

        return new AlarmResponse(
                alarm.getId(),
                sendUser.getId(),
                sendUser.getId(),
                alarm.getType(),
                alarm.getItemId(),
                alarm.getMessage(),
                alarm.getCreatedAt());
    }

    @Transactional
    public List<AlarmResponse> getAlarms() {

        return alarmRepository.findAll().stream()
                .map(alarm -> new AlarmResponse(
                        alarm.getIdRead(),  // read -> true 변경
                        alarm.getReceiveUser().getId(),
                        alarm.getSendUser().getId(),
                        alarm.getType(),
                        alarm.getItemId(),
                        alarm.getMessage(),
                        alarm.getCreatedAt())).toList();
    }

    private String typeMessage(String type) {
        return switch (type) {
            case "MEMBER" -> "맴버를 추가하였습니다.";
            case "CARD" -> "카드를 추가하였습니다.";
            case "COMMENT" -> "댓글을 추가하였습니다.";
            default -> throw new IllegalArgumentException("올바르지 못한 타입: " + type);
        };
    }
}
