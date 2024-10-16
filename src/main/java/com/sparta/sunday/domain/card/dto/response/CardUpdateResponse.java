package com.sparta.sunday.domain.card.dto.response;

import com.sparta.sunday.domain.card.entity.Card;
import com.sparta.sunday.domain.card.entity.CardActivity;
import com.sparta.sunday.domain.card.entity.CardManager;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CardUpdateResponse {

    private final Long id;
    private final String title;
    private final String description;
    private final LocalDateTime dueTo;
    private final List<String> cardManagers;
    private final List<String> activities;

    public CardUpdateResponse(Card card, List<CardActivity> activities) {
        this.id = card.getId();
        this.title = card.getTitle();
        this.description = card.getDescription();
        this.dueTo = card.getDueTo();

        // 매니저 리스트
        this.cardManagers = card.getCardManagerList().stream()
                .map(CardManager::getManagerName)
                .collect(Collectors.toList());

        this.activities = activities.stream()
                .map(CardActivity::getAction)
                .collect(Collectors.toList());
    }
}
