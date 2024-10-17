package com.sparta.sunday.domain.card.dto.response;

import com.sparta.sunday.domain.attachment.dto.response.UploadAttachmentResponse;
import com.sparta.sunday.domain.card.entity.Card;
import com.sparta.sunday.domain.card.entity.CardActivity;
import com.sparta.sunday.domain.card.entity.CardManager;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CardResponse {

    private final Long id;
    private final String title;
    private final String description;
    private final LocalDateTime dueTo;
    private final List<String> cardManagers;
    private final List<CardActivity> activities;
    private final UploadAttachmentResponse attachmentResponse;

    public CardResponse(Card card, UploadAttachmentResponse attachmentResponse) {
        this.id = card.getId();
        this.title = card.getTitle();
        this.description = card.getDescription();
        this.dueTo = card.getDueTo();
        this.cardManagers = card.getCardManagerList().stream()
                .map(CardManager::getManagerName)
                .collect(Collectors.toList());
        this.activities = new ArrayList<>(card.getActivities());
        this.attachmentResponse = attachmentResponse;
    }
}
