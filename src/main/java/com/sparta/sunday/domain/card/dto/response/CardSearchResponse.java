package com.sparta.sunday.domain.card.dto.response;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class CardSearchResponse {
    private final Long id;
    private final String title;
    private final String description;
    private final LocalDateTime dueTo;


    public CardSearchResponse(Long id, String title, String description, LocalDateTime dueTo) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dueTo = dueTo;

    }
}
