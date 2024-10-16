package com.sparta.sunday.domain.card.controller;

import com.sparta.sunday.domain.card.dto.request.CardRequest;
import com.sparta.sunday.domain.card.dto.response.CardDetailResponse;
import com.sparta.sunday.domain.card.dto.response.CardResponse;
import com.sparta.sunday.domain.card.dto.response.CardUpdateResponse;
import com.sparta.sunday.domain.card.service.CardService;
import com.sparta.sunday.domain.common.dto.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/{workspaceId}/lists/{listId}/cards")
public class CardController {

    private final CardService cardService;

    @PostMapping
    public ResponseEntity<CardResponse> createCard(
            @PathVariable Long workspaceId,
            @PathVariable Long listId,
            @RequestBody CardRequest cardRequest,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        return ResponseEntity.ok(cardService.createCard(workspaceId, listId, cardRequest, authUser));
    }

    @PutMapping("/{cardId}")
    public ResponseEntity<CardUpdateResponse> updateCard(
            @PathVariable Long workspaceId,
            @PathVariable Long cardId,
            @RequestBody CardRequest cardRequest,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        return ResponseEntity.ok(cardService.upadteCard(workspaceId, cardId, cardRequest, authUser));
    }

    @GetMapping("{cardId}")
    public ResponseEntity<CardDetailResponse> getCardDetails(
            @PathVariable Long cardId) {
        CardDetailResponse cardDetailResponse = cardService.findCardWithDetails(cardId);
        return ResponseEntity.ok(cardDetailResponse);
    }
}
