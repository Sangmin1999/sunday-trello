package com.sparta.sunday.domain.card.service;

import com.sparta.sunday.domain.card.dto.request.CardRequest;
import com.sparta.sunday.domain.card.dto.response.CardResponse;
import com.sparta.sunday.domain.card.dto.response.CardUpdateResponse;
import com.sparta.sunday.domain.card.entity.Card;
import com.sparta.sunday.domain.card.entity.CardActivity;
import com.sparta.sunday.domain.card.entity.CardManager;
import com.sparta.sunday.domain.card.repository.CardActivityRepository;
import com.sparta.sunday.domain.card.repository.CardManagerRepository;
import com.sparta.sunday.domain.card.repository.CardRepository;
import com.sparta.sunday.domain.common.dto.AuthUser;
import com.sparta.sunday.domain.common.validator.AuthorizationValidator;
import com.sparta.sunday.domain.list.entity.BoardList;
import com.sparta.sunday.domain.list.repository.ListRepository;
import com.sparta.sunday.domain.user.entity.User;
import com.sparta.sunday.domain.user.repository.UserRepository;
import com.sparta.sunday.domain.workspace.enums.WorkspaceRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CardService {

    private final CardRepository cardRepository;
    private final ListRepository listRepository;
    private final CardActivityService cardActivityService;
    private final CardActivityRepository cardActivityRepository;
    private final CardManagerRepository cardManagerRepository;
    private final UserRepository userRepository;
    private final AuthorizationValidator authorizationValidator;

    @Transactional
    public CardResponse createCard(Long workspaceId, Long listId, CardRequest cardRequest, AuthUser authUser) {
        authorizationValidator.checkWorkspaceAuthorization(authUser.getUserId(), workspaceId, WorkspaceRole.MEMBER);

        BoardList boardList = listRepository.findById(listId)
                .orElseThrow(() -> new IllegalArgumentException("해당 리스트가 없습니다"));

        Card card = new Card(
                cardRequest.getTitle(),
                cardRequest.getDescription(),
                LocalDateTime.parse(cardRequest.getDueTo()),
                boardList
        );

        Card savedCard = cardRepository.save(card);

        addManagerToCard(savedCard, cardRequest.getManagerEmail());
        cardActivityService.logCardActivity(savedCard, "카드 생성", authUser);

        return new CardResponse(savedCard);
    }

    @Transactional
    public CardUpdateResponse upadteCard(Long workspaceId, Long cardId, CardRequest cardRequest, AuthUser authUser) {
        authorizationValidator.checkWorkspaceAuthorization(authUser.getUserId(), workspaceId, WorkspaceRole.MEMBER);

        Card card = cardRepository.findCardWithManagers(cardId)
                .orElseThrow(() -> new IllegalArgumentException("해당 카드를 찾을 수 없습니다."));

        List<CardActivity> activities = cardActivityRepository.findCardActivitiesByCardId(cardId);

        card.update(
                cardRequest.getTitle(),
                cardRequest.getDescription(),
                LocalDateTime.parse(cardRequest.getDueTo())
        );

        addManagerToCard(card, cardRequest.getManagerEmail());
        cardActivityService.logCardActivity(card, "카드 수정", authUser);

        return new CardUpdateResponse(card,activities);

    }

    private void addManagerToCard(Card card, String managerEmail) {
        User user = userRepository.findByEmail(managerEmail)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

        CardManager cardManager = new CardManager(card, user);
        cardManagerRepository.save(cardManager); // 매니저 저장
        card.addManager(cardManager); // Card 엔티티에 매니저 추가
    }
}
