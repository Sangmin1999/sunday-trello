package com.sparta.sunday.domain.card.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.slack.api.methods.SlackApiException;
import com.sparta.sunday.domain.attachment.dto.response.UploadAttachmentResponse;
import com.sparta.sunday.domain.attachment.service.AttachmentService;
import com.sparta.sunday.domain.card.entity.CardAttachment;
import com.sparta.sunday.domain.card.repository.CardAttachmentRepository;
import com.sparta.sunday.domain.card.dto.request.CardRequest;
import com.sparta.sunday.domain.card.dto.response.CardDetailResponse;
import com.sparta.sunday.domain.card.dto.response.CardResponse;
import com.sparta.sunday.domain.card.dto.response.CardUpdateResponse;
import com.sparta.sunday.domain.card.entity.Card;
import com.sparta.sunday.domain.card.entity.CardActivity;
import com.sparta.sunday.domain.card.entity.CardManager;
import com.sparta.sunday.domain.card.repository.CardActivityRepository;
import com.sparta.sunday.domain.card.repository.CardManagerRepository;
import com.sparta.sunday.domain.card.repository.CardRepository;
import com.sparta.sunday.domain.comment.entity.Comment;
import com.sparta.sunday.domain.comment.repository.CommentRepository;
import com.sparta.sunday.domain.common.dto.AuthUser;
import com.sparta.sunday.domain.common.validator.AuthorizationValidator;
import com.sparta.sunday.domain.list.entity.BoardList;
import com.sparta.sunday.domain.list.repository.ListRepository;
import com.sparta.sunday.domain.user.entity.User;
import com.sparta.sunday.domain.user.repository.UserRepository;
import com.sparta.sunday.domain.workspace.enums.WorkspaceRole;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
    private final CardAttachmentRepository cardAttachmentRepository;
    private final CommentRepository commentRepository;
    private final AttachmentService attachmentService;
    private final AmazonS3Client amazonS3Client;

    @Value("${bucketName}")
    private String bucketName;

    @Transactional
    public CardResponse createCard(Long workspaceId, Long listId, CardRequest cardRequest, MultipartFile file, AuthUser authUser) throws SlackApiException, IOException {
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

        UploadAttachmentResponse attachmentResponse = null;
        if (file != null && !file.isEmpty()) {
            attachmentResponse = attachmentService.uploadAttachment(file, savedCard.getId(), workspaceId, authUser).getBody();
        }

        return new CardResponse(savedCard, attachmentResponse);
    }

    @Transactional
    public CardUpdateResponse upadteCard(Long workspaceId, Long cardId, CardRequest cardRequest, MultipartFile file, AuthUser authUser) { //throws SlackApiException, IOException {
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

        UploadAttachmentResponse attachmentResponse = null;
        if (file != null && !file.isEmpty()) {
            // 1. 기존 첨부파일 조회
            List<CardAttachment> existingAttachments = card.getCardAttachments(); // 카드의 첨부파일 리스트 가져오기
            for (CardAttachment attachment : existingAttachments) {
                // 2. S3에서 파일 삭제
                amazonS3Client.deleteObject(bucketName, attachment.getFileName()); // S3에서 기존 파일 삭제
                // 3. 데이터베이스에서 첨부파일 엔티티 삭제
                cardAttachmentRepository.delete(attachment);
            }

            // 새 파일 업로드
            attachmentResponse = attachmentService.uploadAttachment(file, card.getId(), workspaceId, authUser).getBody();
        }

        return new CardUpdateResponse(card,activities,attachmentResponse);

    }

    public CardDetailResponse findCardWithDetails(Long cardId) {

        Card card = cardRepository.findCardWithDetails(cardId)
                .orElseThrow(() -> new IllegalArgumentException("해당 카드를 찾을 수 없습니다."));

        List<CardManager> cardManagers = cardManagerRepository.findCardManagersByCardId(cardId);
        List<Long> managerIds = cardManagers.stream()
                .map(cardManager -> cardManager.getUser().getId())
                .collect(Collectors.toList());

        List<Comment> comments = commentRepository.findCommentsByCardId(cardId);
        List<String> commentContents = comments.stream()
                .map(Comment::getContent)
                .collect(Collectors.toList());

        List<String> activities = cardActivityRepository.findGetActivitiesByCardId(cardId);
        List<String> attachments = cardAttachmentRepository.findAttachmentsByCardId(cardId);

        return new CardDetailResponse(
                card.getId(),
                card.getTitle(),
                card.getDescription(),
                card.getDueTo(),
                managerIds,
                activities,
                card.getBoardList().getId(),
                commentContents,
                attachments
        );
    }

    @Transactional
    public void deleteCard(Long workspaceId, Long cardId, AuthUser authUser) {

        authorizationValidator.checkWorkspaceAuthorization(authUser.getUserId(), workspaceId, WorkspaceRole.MEMBER);

        Card card = cardRepository.findCardWithDetails(cardId)
                .orElseThrow(() -> new IllegalArgumentException("해당 카드를 찾을 수 없습니다."));

        cardRepository.delete(card);
    }

    private void addManagerToCard(Card card, String managerEmail) {
        User user = userRepository.findByEmail(managerEmail)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

        CardManager cardManager = new CardManager(card, user);
        cardManagerRepository.save(cardManager); // 매니저 저장
        card.addManager(cardManager); // Card 엔티티에 매니저 추가
    }

}
