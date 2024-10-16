package com.sparta.sunday.domain.card.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CardRequest {

    private String title;
    private String description;
    private String dueTo;
    private String managerEmail;
}
