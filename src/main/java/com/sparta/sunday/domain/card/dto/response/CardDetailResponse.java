package com.sparta.sunday.domain.card.dto.response;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class CardDetailResponse {

    private final Long id;
    private final String title;
    private final String description;
    private final LocalDateTime dueTo;
    private final Long managerId;
    private final List<String> activities;
    private final Long listId;
    private final List<String> comments; // 댓글 리스트 추가
    private final List<String> attachments;

    public CardDetailResponse(Long id, String title, String description, LocalDateTime dueTo, Long managerId,
                        List<String> activities, Long listId, List<String> comments, List<String> attachments) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dueTo = dueTo;
        this.managerId = managerId;
        this.activities = activities;
        this.listId = listId;
        this.comments = comments;
        this.attachments = attachments;
    }
}
