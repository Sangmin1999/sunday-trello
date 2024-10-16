package com.sparta.sunday.domain.card.service;

import com.sparta.sunday.domain.card.entity.Card;
import com.sparta.sunday.domain.card.entity.CardActivity;
import com.sparta.sunday.domain.card.repository.CardActivityRepository;
import com.sparta.sunday.domain.common.dto.AuthUser;
import com.sparta.sunday.domain.user.entity.User;
import com.sparta.sunday.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CardActivityService {

    private final CardActivityRepository cardActivityRepository;
    private final UserRepository userRepository;

    @Transactional
    public void logCardActivity(Card card, String action, AuthUser authUser) {

        User user = userRepository.findById(authUser.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        CardActivity cardActivity = new CardActivity(card, action, user);
        cardActivityRepository.save(cardActivity);
    }
}
