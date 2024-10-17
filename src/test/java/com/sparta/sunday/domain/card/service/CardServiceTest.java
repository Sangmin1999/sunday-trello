package com.sparta.sunday.domain.card.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.slack.api.methods.SlackApiException;
import com.sparta.sunday.domain.attachment.dto.response.UploadAttachmentResponse;
import com.sparta.sunday.domain.attachment.service.AttachmentService;
import com.sparta.sunday.domain.card.dto.request.CardRequest;
import com.sparta.sunday.domain.card.dto.response.CardDetailResponse;
import com.sparta.sunday.domain.card.dto.response.CardUpdateResponse;
import com.sparta.sunday.domain.card.entity.Card;
import com.sparta.sunday.domain.card.entity.CardAttachment;
import com.sparta.sunday.domain.card.repository.CardActivityRepository;
import com.sparta.sunday.domain.card.repository.CardAttachmentRepository;
import com.sparta.sunday.domain.card.repository.CardManagerRepository;
import com.sparta.sunday.domain.card.repository.CardRepository;
import com.sparta.sunday.domain.comment.repository.CommentRepository;
import com.sparta.sunday.domain.common.dto.AuthUser;
import com.sparta.sunday.domain.common.validator.AuthorizationValidator;
import com.sparta.sunday.domain.list.entity.BoardList;
import com.sparta.sunday.domain.list.repository.ListRepository;
import com.sparta.sunday.domain.user.entity.User;
import com.sparta.sunday.domain.user.enums.UserRole;
import com.sparta.sunday.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
public class CardServiceTest {

    @Mock
    private CardRepository cardRepository;
    @Mock
    private CardManagerRepository cardManagerRepository;
    @Mock
    private ListRepository listRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CardActivityRepository cardActivityRepository;
    @Mock
    private CardAttachmentRepository cardAttachmentRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private AttachmentService attachmentService;
    @Mock
    private AuthorizationValidator authorizationValidator;
    @Mock
    private AmazonS3Client amazonS3Client;
    @Mock
    private CardActivityService cardActivityService;

    @InjectMocks
    private CardService cardService;

    private Card mockCard;
    private BoardList mockBoardList;
    private CardRequest mockCardRequest;
    private AuthUser mockAuthUser;
    private User mockUser;

    @BeforeEach
    public void setup() {

        mockBoardList = new BoardList("Mock List", 1, null);
        ReflectionTestUtils.setField(mockBoardList, "id", 1L);

        mockCard = new Card("Mock Card", "Description", LocalDateTime.now(), mockBoardList);
        ReflectionTestUtils.setField(mockCard, "id", 1L);

        mockCardRequest = new CardRequest();
        ReflectionTestUtils.setField(mockCardRequest, "title", "Mock Card");
        ReflectionTestUtils.setField(mockCardRequest, "description", "Description");
        ReflectionTestUtils.setField(mockCardRequest, "dueTo", LocalDateTime.now().toString());
        ReflectionTestUtils.setField(mockCardRequest, "managerEmail", "manager@example.com");

        mockAuthUser = new AuthUser(1L, "user@example.com", UserRole.ROLE_ADMIN);

        List<CardAttachment> mockAttachments = new ArrayList<>();
        CardAttachment attachment = new CardAttachment("jpg", 100L, "/path/to/file", "test.jpg", mockAuthUser.getUserId(), mockCard);
        mockAttachments.add(attachment);
        mockCard.getCardAttachments().addAll(mockAttachments);

        mockUser = new User(1L, "manager@example.com", UserRole.ROLE_ADMIN);
    }

    @Test
    public void 카드_생성_리스트_없음() {
        // Given
        given(listRepository.findById(anyLong())).willReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> cardService.createCard(1L, 1L, mockCardRequest, null, mockAuthUser))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("해당 리스트가 없습니다");

        verify(cardRepository, never()).save(any(Card.class));
    }

    @Test
    public void 카드_수정_성공() throws IOException, SlackApiException {
        // Given
        given(cardRepository.findCardWithManagers(anyLong())).willReturn(Optional.of(mockCard));

        // Mock 유저 설정
        User mockManager = new User(2L, "manager@example.com", UserRole.ROLE_ADMIN);

        // Mock 파일 생성
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test".getBytes());

        // 첨부파일 업로드 시 반환값 설정
        UploadAttachmentResponse mockAttachmentResponse = new UploadAttachmentResponse(
                mockCard.getId(),
                file.getOriginalFilename(),
                mockAuthUser.getUserId(),
                new URL("http://test-url.com")
        );
        given(attachmentService.uploadAttachment(eq(file), eq(mockCard.getId()), anyLong(), eq(mockAuthUser)))
                .willReturn(ResponseEntity.ok(mockAttachmentResponse));

        given(userRepository.findByEmail(any())).willReturn(Optional.ofNullable(mockUser));

        // When
        CardUpdateResponse response = cardService.updateCard(1L, 1L, mockCardRequest, file, mockAuthUser);

        // Then
        assertThat(response.getTitle()).isEqualTo("Mock Card");
        verify(cardRepository).findCardWithManagers(anyLong());
        verify(cardAttachmentRepository).delete(any(CardAttachment.class)); // 첨부파일 삭제 여부
        verify(attachmentService).uploadAttachment(eq(file), eq(mockCard.getId()), anyLong(), eq(mockAuthUser));
    }

    @Test
    public void 카드_수정_카드_없음() {
        // Given
        given(cardRepository.findCardWithManagers(anyLong())).willReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> cardService.updateCard(1L, 1L, mockCardRequest, null, mockAuthUser))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("해당 카드를 찾을 수 없습니다");

        verify(cardRepository, never()).save(any(Card.class));
    }

    @Test
    public void 카드_상세_조회_성공() {
        // Given
        given(cardRepository.findCardWithDetails(anyLong())).willReturn(Optional.of(mockCard));
        given(cardManagerRepository.findCardManagersByCardId(anyLong())).willReturn(List.of());
        given(commentRepository.findCommentsByCardId(anyLong())).willReturn(List.of());

        // When
        CardDetailResponse response = cardService.findCardWithDetails(1L);

        // Then
        assertThat(response.getTitle()).isEqualTo("Mock Card");
        verify(cardRepository).findCardWithDetails(anyLong());
        verify(cardManagerRepository).findCardManagersByCardId(anyLong());
        verify(commentRepository).findCommentsByCardId(anyLong());
    }

    @Test
    public void 카드_상세_조회_카드_없음() {
        // Given
        given(cardRepository.findCardWithDetails(anyLong())).willReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> cardService.findCardWithDetails(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("해당 카드를 찾을 수 없습니다");

        verify(cardRepository).findCardWithDetails(anyLong());
        verify(cardManagerRepository, never()).findCardManagersByCardId(anyLong());
    }

}
