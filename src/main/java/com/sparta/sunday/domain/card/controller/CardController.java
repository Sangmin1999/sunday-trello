package com.sparta.sunday.domain.card.controller;

import com.slack.api.methods.SlackApiException;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/{workspaceId}/lists/{listId}/cards")
public class CardController {

    private final CardService cardService;

    @PostMapping
    public ResponseEntity<CardResponse> createCard(
            @PathVariable Long workspaceId,
            @PathVariable Long listId,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestBody CardRequest cardRequest,
            @AuthenticationPrincipal AuthUser authUser
    ) throws SlackApiException, IOException {
        return ResponseEntity.ok(cardService.createCard(workspaceId, listId, cardRequest, file, authUser));
    }


    @PutMapping("/{cardId}")
    public ResponseEntity<CardUpdateResponse> updateCard(
            @PathVariable Long workspaceId,
            @PathVariable Long cardId,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestBody CardRequest cardRequest,
            @AuthenticationPrincipal AuthUser authUser
    ) { //throws SlackApiException, IOException {
        return ResponseEntity.ok(cardService.upadteCard(workspaceId, cardId, cardRequest, file, authUser));
    }

    @GetMapping("{cardId}")
    public ResponseEntity<CardDetailResponse> getCardDetails(
            @PathVariable Long cardId) {
        CardDetailResponse cardDetailResponse = cardService.findCardWithDetails(cardId);
        return ResponseEntity.ok(cardDetailResponse);
    }

    @DeleteMapping("/{cardId}")
    public ResponseEntity<String> deleteCard(
            @PathVariable Long workspaceId,
            @PathVariable Long listId,
            @PathVariable Long cardId,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        cardService.deleteCard(workspaceId, cardId, authUser);
        return ResponseEntity.ok("카드가 성공적으로 삭제되었습니다.");
    }
}
