package com.sparta.sunday.domain.card.service;

import com.slack.api.methods.SlackApiException;
import com.sparta.sunday.domain.alarm.entity.AlarmType;
import com.sparta.sunday.domain.alarm.service.AlarmService;
import com.sparta.sunday.domain.card.entity.Card;
import com.sparta.sunday.domain.card.entity.CardActivity;
import com.sparta.sunday.domain.card.repository.CardActivityRepository;
import com.sparta.sunday.domain.common.dto.AuthUser;
import com.sparta.sunday.domain.user.entity.User;
import com.sparta.sunday.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class CardActivityService {

    private final CardActivityRepository cardActivityRepository;
    private final UserRepository userRepository;
    private final AlarmService alarmService;

    @Transactional
    public void logCardActivity(Card card, String action, AuthUser authUser) throws SlackApiException, IOException {

        User user = userRepository.findById(authUser.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        CardActivity cardActivity = new CardActivity(card, action, user);
        cardActivityRepository.save(cardActivity);

        //alarmService.saveAlarm(AlarmType.CARD, card.getId(), user, card.getActivities().get(0).getUser().getEmail());
    }
}
