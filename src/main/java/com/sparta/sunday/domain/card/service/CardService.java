package com.sparta.sunday.domain.card.service;

import com.sparta.sunday.domain.card.dto.request.CardRequest;
import com.sparta.sunday.domain.card.dto.response.CardResponse;
import com.sparta.sunday.domain.card.entity.Card;
import com.sparta.sunday.domain.card.entity.CardManager;
import com.sparta.sunday.domain.card.repository.CardManagerRepository;
import com.sparta.sunday.domain.card.repository.CardRepository;
import com.sparta.sunday.domain.common.dto.AuthUser;
import com.sparta.sunday.domain.common.exception.UnAuthorizedException;
import com.sparta.sunday.domain.list.entity.BoardList;
import com.sparta.sunday.domain.list.repository.ListRepository;
import com.sparta.sunday.domain.user.entity.User;
import com.sparta.sunday.domain.user.repository.UserRepository;
import com.sparta.sunday.domain.workspace.enums.WorkspaceRole;
import com.sparta.sunday.domain.workspace.repository.WorkspaceMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CardService {

    private final CardRepository cardRepository;
    private final ListRepository listRepository;
    private final CardActivityService cardActivityService;
    private final CardManagerRepository cardManagerRepository;
    private final UserRepository userRepository;
    private final WorkspaceMemberRepository workspaceMemberRepository;

    @Transactional
    public CardResponse createCard(Long listId, CardRequest cardRequest, AuthUser authUser) {
        checkManagerAuthorization(authUser);

        BoardList boardList = listRepository.findById(listId)
                .orElseThrow(() -> new IllegalArgumentException("해당 리스트가 없습니다"));

        System.out.println(cardRequest.getDueTo()+ "gg");
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

    private void addManagerToCard(Card card, String managerEmail) {
        User user = userRepository.findByEmail(managerEmail)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

        CardManager cardManager = new CardManager(card, user);
        cardManagerRepository.save(cardManager); // 매니저 저장
        card.addManager(cardManager); // Card 엔티티에 매니저 추가
    }

    private void checkManagerAuthorization(AuthUser authUser) {
        workspaceMemberRepository.findByMemberIdAndRole(authUser.getUserId(), WorkspaceRole.MANAGER)
                .orElseThrow(() -> new UnAuthorizedException("MANAGER 권한이 필요합니다."));
    }
}
